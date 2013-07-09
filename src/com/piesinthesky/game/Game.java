package com.piesinthesky.game;

import java.net.URL;
import java.util.LinkedList;

import game.engine.Engine;
import game.engine.FadingDeathAnimation;
import game.engine.Float2;
import game.engine.ScalingBehaviour;
import game.engine.Sprite;
import game.engine.Texture;
import game.engine.ThrobAnimation;
import game.engine.Timer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.piesinthesky.game.controller.LevelController;
import com.piesinthesky.game.controller.PlayerController;

public class Game extends Engine{
	private Resources res;
	private Timer timer;
	private Point canvasSize;
	private PlayerController playerController;
	private LevelController levelController;

    // Shutdown listener
	private ShutdownReceiver mShutdownReceiver;
	
	// Dialogs
	private AlertDialog pauseMenu;
	private AlertDialog popupDialog;
	private boolean bGameStarted;
	private boolean bGameOver;
	
	private Sprite gameOverSS;
	@Override
	public void init() {
        // Set shutdown receiver
        mShutdownReceiver = new ShutdownReceiver();
        IntentFilter shutdownIntent = new IntentFilter();
        shutdownIntent.addAction(getString(R.string.shutdown_intent_action));
        registerReceiver(mShutdownReceiver, shutdownIntent);
		setScreenOrientation(Engine.ScreenModes.LANDSCAPE);
		res = getResources();
		timer = new Timer();
		instance = this;
		setupPauseMenu();
		setupPopupDialog();
		bGameStarted = false;
		bGameOver = false;
	}

	@Override
	public void load() {
		playerController = new PlayerController();
		levelController = new LevelController();
		setPaused(true);
		// Show pre level screen
		popupDialog.show();
	}

	@Override
	public void draw() {
		if(!bGameOver) {
			drawBackground();
			levelController.draw();
			playerController.drawPlayer();
		} else {
			canvasSize = new Point(getScreenWidth(), getScreenHeight());
			Paint background = new Paint();
			background.setColor(Color.BLACK);
			getCanvas().drawPaint(background);
			gameOverSS.draw();
		}
	}
	
	public Bitmap toGrayscale(Bitmap bmpOriginal)
	{        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
	}

	private void drawBackground() {
		canvasSize = new Point(getScreenWidth(), getScreenHeight());
		Paint gradient = new Paint();
		gradient.setDither(true);
		gradient.setShader(new LinearGradient(0, 0, 0, (float) (getCanvas().getHeight() * 0.8), res.getColor(R.color.sky_blue), Color.WHITE, Shader.TileMode.CLAMP));
		getCanvas().drawPaint(gradient);
	}

	@Override
	public void update() {
		if(!bGameOver) {
			playerController.animatePlayer();
			levelController.updateLevel();
		} else {
			gameOverSS.animate();
		}
	}

	@Override
	public void collision(Sprite player) {
		Log.d("Game", "Player died!");
		end(player);
	}

	protected void end(Sprite player) {
		player.removeAnimations();
		//player.addAnimation(new FadingDeathAnimation());

		button1.setClickable(false);
		button2.setClickable(false);
		levelController.end();
		
		gameOverSS = Engine.createSprite();
		Texture ss = new Texture(getApplicationContext());
		ss.loadFromBitmap(toGrayscale(getScreenShot()));
		gameOverSS.setTexture(ss);
		gameOverSS.setPosition(new Float2(0,0));
		bGameOver = true;
		gameOverSS.addAnimation(new ScalingBehaviour(0.5f, 0.1f));
		//Intent endScreen = new Intent(this, EndScreen.class);
		//startActivityForResult(endScreen, 1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == 1) {
	     if(resultCode == RESULT_OK){      
	         boolean restart = data.getBooleanExtra("restart", false);
	         
	         if(restart) {
	        	 // restart game
	        	 
	         } else {
	        	 finish();
	         }
	     }
	  }
	}
	
	protected void initializeButtons(){
		super.initializeButtons();
		
		button1.setText(R.string.btn_one);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playerController.playerJump();
			}
		});
		
		button2.setText(R.string.btn_two);
		button2.setClickable(false);
	}
	
	@Override
	protected void collisionTest(){
		LinkedList<Sprite> playerSpriteList = new LinkedList<Sprite>();
		playerSpriteList.add(playerController.getSprite());
		collisionTest(playerSpriteList, levelController.getObstacles());
	}
	
	@Override
	protected void collisionCleanUp(){
		LinkedList<Sprite> playerSpriteList = new LinkedList<Sprite>();
		playerSpriteList.add(playerController.getSprite());
		collisionCleanUp(playerSpriteList);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d("Game","pressed back button");
	        onPauseBehaviour();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResumeBehaviour() {
		// Do nothing, unpausing is handled by pause menu
	}

	@Override
	protected void onPauseBehaviour() {
		if(!isPaused()) {
			// Superclass sets paused state, and increments pause counter
			super.onPauseBehaviour();
			openPauseScreen();
		}
	}
	
	protected void onUnpauseBehaviour() {
		setPaused(false);
		pauseMenu.dismiss();
	}
	
	private void openPauseScreen() {
		if(pauseMenu != null && !pauseMenu.isShowing()) {
			Log.d("Game","showing pause menu");
			pauseMenu.show();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		Log.d("Game","New Game Level:" + getIntent().getIntExtra("gameLevel", -1));
		
		if(getIntent().getIntExtra("gameLevel", -1) != -1) {
			button1.setClickable(true);
			setPaused(true);
			// Show pre level screen
			levelController.resetLevel();
			playerController.resetPlayer();
			popupDialog.show();
		}
		
	}
	
	protected void setupPauseMenu() {
		AlertDialog.Builder menuBuilder = new AlertDialog.Builder(this);
		menuBuilder.setTitle("Game Paused");
		menuBuilder.setMessage("Return to main menu?");

		menuBuilder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onUnpauseBehaviour();
			}
		});
		
		menuBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			Intent intent = new Intent();
			intent.setClass(instance, MainMenu.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		  }
		});

		menuBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    onUnpauseBehaviour();
		  }
		});

		pauseMenu = menuBuilder.create();
	}
	
	protected void setupPopupDialog() {
		AlertDialog.Builder menuBuilder = new AlertDialog.Builder(this);
		menuBuilder.setTitle("Level 1");
		menuBuilder.setMessage("Click start to begin");

		menuBuilder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				((Game)Game.getInstance()).setPaused(false);
				popupDialog.dismiss();
			}
		});

		menuBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			((Game)Game.getInstance()).setPaused(false);
			popupDialog.dismiss();
		  }
		});

		popupDialog = menuBuilder.create();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// Force a redraw of the canvas after app resumes from background
		enableForceRedraw();
	}
	
    private final class ShutdownReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Game.this.setResult(Activity.RESULT_OK);
            finish();
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	Log.d("Game","Game Level:" + getIntent().getIntExtra("gameLevel", 1));
    	getIntent().removeExtra("gameLevel");
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	//pauseMenu.dismiss();
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
        if (pauseMenu !=null && pauseMenu.isShowing()){
        	pauseMenu.dismiss();
        }
    	levelController.destroy();
    	playerController.destroy();
        unregisterReceiver(mShutdownReceiver);
        System.runFinalizersOnExit(true);
        System.gc();
    }
}
