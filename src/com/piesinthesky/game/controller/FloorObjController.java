package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;
import game.engine.Sprite;
import game.engine.Texture;
import game.engine.WarpBehavior;
import android.graphics.Point;
import android.graphics.RectF;

import com.piesinthesky.game.R;
import com.piesinthesky.game.TexturePair;

public class FloorObjController extends ObjController{
	private int nFloorCounter;
	private int nAvailableTiles;
	private final int SCREEN_HEIGHT = 295;
	
	// Texture Assets
	public static final String DIRT_BLOCK = "dirt_block.png";
	public static final String STONE_BLOCK = "stone_block.png";
	
	public FloorObjController(RectF boundary, Float2 vel, int blockWidth, int nAvailableTiles) {
		super(boundary, vel, blockWidth);
		
		bAnimating = true;
		this.nAvailableTiles = nAvailableTiles;
		nFloorCounter = 0;
		
		setupSprites();
	}
	
	public void animate()
	{
		if(bAnimating)
		{
			for(int i = 0; i < spriteList.size(); i++) {
				Sprite floorTile = spriteList.get(i);
				Float2 prevPos = new Float2(floorTile.getPosition().x, floorTile.getPosition().y);
	
				floorTile.animate();
				
				Float2 curPos = floorTile.getPosition();
				RectF tileBounds = floorTile.getBounds();
				
				if(!floorTile.getVisible() && (tileBounds.right > 0 || tileBounds.left < Engine.getInstance().getScreenWidth() ||
						tileBounds.bottom > 0 || tileBounds.top < Engine.getInstance().getScreenHeight())) {
					spritePool.removeFirst();
					floorTile.setVisible(true);
				}

				if((curPos.x < prevPos.x && vel.x > 0) || (curPos.y < prevPos.y && vel.y > 0) ||
					(curPos.x > prevPos.x && vel.x < 0) || (curPos.y > prevPos.y && vel.y < 0))
				{
					nFloorCounter++;
					spritePool.add(floorTile);
					floorTile.setVisible(false);
				}
			}
		}
	}

	public int getFloorCount(){
		return nFloorCounter;
	}

	@Override
	protected void setupTextureList() {
		textureList = new TexturePair[2];
		textureList[0] = new TexturePair(R.drawable.dirt_block);
		textureList[1] = new TexturePair(R.drawable.stone_block);
	}

	@Override
	protected void setupSprites() {
		Texture texture = textureList[0].getTexture();
		
		for(int i = 0; i < nAvailableTiles; i++) {
			Sprite floorTile = Engine.createSprite();

			floorTile.setTexture(texture);
			floorTile.setPosition(new Float2(0 + (i * nBlockWidth), boundary.bottom - floorTile.getHeight()));
			floorTile.addAnimation(new WarpBehavior(boundary, floorTile.getSize(), vel, true));
			floorTile.setCollidable(false);
			spriteList.add(floorTile);
		}
	}
	
	public int getNumberOfTiles() {
		return spriteList.size();
	}
}
