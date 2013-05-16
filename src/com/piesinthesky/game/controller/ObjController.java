package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;
import game.engine.Sprite;

import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.RectF;

import com.piesinthesky.game.TexturePair;

public abstract class ObjController {
	protected TexturePair[] textureList;
	protected LinkedList<Sprite> spritePool;
	protected LinkedList<Sprite> spriteList;
	protected boolean bAnimating;
	
	protected RectF boundary;
	protected Float2 vel;
	protected int blockWidth;

	public ObjController(RectF boundary, Float2 vel, int blockWidth) {
		setupTextureList();
		loadTextures();
		
		this.boundary = boundary;
		this.vel = vel;
		this.blockWidth = blockWidth;
		bAnimating = false;
		spritePool = new LinkedList<Sprite>();
		spriteList = new LinkedList<Sprite>();
	}
	
	protected abstract void setupTextureList();
	
	protected void loadTextures(){
		for(int i = 0; i < textureList.length; i++){
			TexturePair pair = textureList[i];
			
			if(pair.getTexture() == null) {
				pair.setTexture(Engine.loadTexture(pair.getName()));
			}
		}
	}
	
	protected abstract void setupSprites();
	
	public abstract void animate();
	
	public boolean command(char command, int param) {
		switch (command) {
			case 'c':
				return changeTexture(param);
			default:
				return false;
		}
	}
	
	public boolean changeTexture(int textureNumber){
		if(!spritePool.isEmpty()) {
			Sprite sprite = spritePool.getFirst();
			TexturePair pair = textureList[textureNumber];
			
			if(!sprite.getTexture().getName().equals(pair.getName())) {
				sprite.setTexture(pair.getTexture());
			}
			
			return true;
		}
		
		return false;
	}
	
	public void draw()
	{
		Iterator<Sprite> iter = spriteList.iterator();
		
		while(iter.hasNext()){
			iter.next().draw();
		}
	}
	
	public boolean getAnimating()
	{
		return bAnimating;
	}
	
	public void setAnimating(boolean animating)
	{
		bAnimating = animating;
	}
}
