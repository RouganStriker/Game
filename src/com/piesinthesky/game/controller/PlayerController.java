package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;
import game.engine.JumpBehavior;
import com.piesinthesky.game.R;
import game.engine.Sprite;
import game.engine.Texture;
import android.content.Context;
import android.graphics.Point;

import com.piesinthesky.game.Game;

public class PlayerController {
	private Sprite player;
	private Engine engine;
	private static final String PLAYER_BLOCK = "character_princess.png";
	private static final Point PLAYER_BLOCK_SIZE = new Point(75, 99);
	
	public PlayerController() {	
		engine = Game.getInstance();
		setupPlayer();
	}
	
	private void setupPlayer() {
		Texture playerTexture = Engine.loadTexture(R.drawable.character_princess);

		player = Engine.createSprite();
		player.setTexture(playerTexture);
		player.setPosition(new Float2(0, Engine.getInstance().getScreenHeight() - PLAYER_BLOCK_SIZE.y - 60));
		player.setCollidable(true);
	}
	
	public void resetPlayer(){
		player.setPosition(new Float2(0, Engine.getInstance().getScreenHeight() - PLAYER_BLOCK_SIZE.y - 60));
		player.setCollidable(true);
		player.setAlpha(255);
	}
	
	public void drawPlayer(){
		player.draw();
	}
	
	public void animatePlayer(){
		player.animate();
	}
	
	public void playerJump(){
		if(player.getAnimations().size() == 0) {
			player.addAnimation(new JumpBehavior(new Float2(0, -15), 0.8f));
			player.animate();
		}
	}
	
	public Sprite getSprite(){
		return player;
	}
	
	public void destroy(){
		player.destroy();
	}
}
