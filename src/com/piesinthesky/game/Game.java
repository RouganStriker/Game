package com.piesinthesky.game;

import java.net.URL;
import java.util.LinkedList;

import game.engine.Engine;
import game.engine.FadingDeathAnimation;
import game.engine.Sprite;
import game.engine.Timer;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
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
		debug_showCollisionBoundaries = true;
		instance = this;
		setupPauseMenu();
		setupPopupDialog();
		bGameStarted = false;
	}

	@Override
	public void load() {
		playerController = new PlayerController();
		levelController = new LevelController();
		setPauseState(true);
		// Show pre level screen
		popupDialog.show();
	}

	@Override
	public void draw() {
		drawBackground();
		levelController.draw();
		playerController.drawPlayer();
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
		playerController.animatePlayer();
		levelController.updateLevel();
	}

	@Override
	public void collision(Sprite player) {
		Log.d("Game", "Player died!");
		player.removeAnimations();
		player.addAnimation(new FadingDeathAnimation());
		button1.setClickable(false);
		button2.setClickable(false);
		levelController.end();
	}
	
	protected void initializeButtons(){
		super.initializeButtons();
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playerController.playerJump();
			}
		});
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
	        onPause();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pauseScreen();
	}

	private void pauseScreen() {
		if(pauseMenu != null && !pauseMenu.isShowing()) {
			Log.d("Game","showing pause menu");
			pauseMenu.show();
		}
	}

	protected void setupPauseMenu() {
		AlertDialog.Builder menuBuilder = new AlertDialog.Builder(this);
		menuBuilder.setTitle("Game Paused");
		menuBuilder.setMessage("Return to main menu?");

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
		    onResume();
		  }
		});

		pauseMenu = menuBuilder.create();
	}
	
	protected void setupPopupDialog() {
		AlertDialog.Builder menuBuilder = new AlertDialog.Builder(this);
		menuBuilder.setTitle("Level 1");
		menuBuilder.setMessage("Click start to begin");

		menuBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			setPauseState(false);
			popupDialog.dismiss();
		  }
		});

		popupDialog = menuBuilder.create();
	}

	protected void onStop() {
		Log.d("Game","game stopped");
        super.onStop();
        if (pauseMenu !=null && pauseMenu.isShowing()){
        	pauseMenu.dismiss();
        }
    }
	
    private final class ShutdownReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	levelController.destroy();
    	playerController.destroy();
        unregisterReceiver(mShutdownReceiver);
        System.runFinalizersOnExit(true);
        System.gc();
    }
}
