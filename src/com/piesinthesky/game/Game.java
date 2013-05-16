package com.piesinthesky.game;

import game.engine.Engine;
import game.engine.Sprite;
import game.engine.Timer;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.view.View;
import android.view.View.OnClickListener;

import com.piesinthesky.game.controller.LevelController;
import com.piesinthesky.game.controller.PlayerController;

public class Game extends Engine{
	private Resources res;
	private Timer timer;
	private Point canvasSize;
	private PlayerController playerController;
	private LevelController levelController;

	@Override
	public void init() {
		setScreenOrientation(Engine.ScreenModes.LANDSCAPE);
		res = getResources();
		timer = new Timer();
		instance = this;
	}

	@Override
	public void load() {
		playerController = new PlayerController();
		levelController = new LevelController();
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
	public void collision(Sprite sprite) {
		// TODO Auto-generated method stub
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
}
