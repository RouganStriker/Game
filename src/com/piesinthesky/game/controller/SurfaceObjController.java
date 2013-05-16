package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;
import game.engine.Sprite;
import game.engine.Texture;
import game.engine.WarpBehavior;

import java.util.ArrayList;
import java.util.List;

import com.piesinthesky.game.TexturePair;

import android.graphics.Point;
import android.graphics.RectF;

public class SurfaceObjController extends ObjController{
	public static final String ROCK_OBJ = "rock_obj.png";
	
	public SurfaceObjController(RectF boundary, Float2 vel, int blockWidth){
		super(boundary, vel, blockWidth);
		
		setupSprites();
	}

	@Override
	protected void setupTextureList() {
		textureList = new TexturePair[1];
		textureList[0] = new TexturePair(ROCK_OBJ);
	}

	@Override
	protected void setupSprites() {
		Sprite spr = Engine.createSprite();
		
		spr.setPosition(new Float2(boundary.right - blockWidth/2, boundary.bottom));
		spr.addAnimation(new WarpBehavior(boundary, spr.getSize(), vel, true));
		spr.setCollidable(true);
		spritePool.add(spr);
	}

	@Override
	public void animate() {
		// TODO Auto-generated method stub
		
	}
}
