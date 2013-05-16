package com.piesinthesky.game.model;

import game.engine.Sprite;
import android.graphics.Rect;

public class Character {
	private String sName;
	private game.engine.Sprite sprite;
	private int nHealth;
	private int nDamage;
	private int nSpeed;
	private Rect hitBox, attackBox;
	private AI ai;
	private Movement movement;
	private boolean isInvincible;
	
	public Character(String sName, int nHealth, int nDamage, int nSpeed, AI ai, Movement movement){
		this.sName = sName;
		this.nHealth = nHealth;
		this.nDamage = nDamage;
		this.nSpeed = nSpeed;
		this.ai = ai;
		this.movement = movement;
	}
	
	public void destroy(){
		
	}
	
	public void action(){
		
	}
	
	public void draw(){
		sprite.draw();
	}
	
	/*
	 * Getters and Setters
	 */
	public void setInvincible(boolean isInvincible){
		this.isInvincible = isInvincible;
	}
	
	public boolean isInvincible(){
		return this.isInvincible;
	}
	
	public void setSprite(Sprite sprite){
		this.sprite = sprite;
	}
}
