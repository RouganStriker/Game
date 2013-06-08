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
	private int nBlockHeight;

	public SurfaceObjController(RectF boundary, Float2 vel, int blockWidth, int blockHeight){
		super(boundary, vel, blockWidth);
		
		nBlockHeight = blockHeight;
		setupSprites();
		bAnimating = true;
	}

	@Override
	protected void setupTextureList() {
		textureList = new TexturePair[1];
		textureList[0] = new TexturePair(ROCK_OBJ);
	}

	@Override
	protected void setupSprites() {
		Sprite spr = Engine.createSprite();
		
		spr.setTexture(textureList[0].getTexture());
		spr.setPosition(new Float2(boundary.right - nBlockWidth, boundary.bottom - spr.getHeight() - nBlockHeight/2));
		spr.addAnimation(new WarpBehavior(boundary, spr.getSize(), vel, true));
		spr.setCollidable(true);
		spriteList.add(spr);
	}

	@Override
	public void animate() {
		if(bAnimating)
		{
			for(int i = 0; i < spriteList.size(); i++) {
				Sprite spr = spriteList.get(i);

				spr.animate();
			}
		}
	}
}
