package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;

import java.util.LinkedList;
import com.piesinthesky.game.Game;

import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

public class LevelController {
	private int nCurrentLevel;
	private int nCurrentFloorTile;
	private LinkedList<String> sInstructionsFloor;
	
	private int nTileMarker;
	private int nTextureId;
	private String sTargetController;
	private String sControllerCommand;

	private int nTileChangeCounter;
	private boolean bEndOfLevel;
	private final String FLOOR_DIRT = "d";
	private final String FLOOR_STONE = "s";
	private final String END_OF_LEVEL = "eol";
	private FloorObjController tileController;

	public LevelController() {
		nCurrentLevel = 1;
		nCurrentFloorTile = 0;
		bEndOfLevel = false;
		nTileChangeCounter = 0;
		
		Point blockSize = new Point(101, 121);
		int nAvailableTiles = Engine.getInstance().getScreenWidth() / blockSize.x + 2;
		int screenHeight = 295;
		RectF boundary = new RectF(0 - blockSize.x, 0 - blockSize.y, blockSize.x * nAvailableTiles, screenHeight);
		Float2 vel = new Float2(-7, 0);

		tileController  = new FloorObjController(boundary, vel, blockSize.x, nAvailableTiles);
		setupInstructionsFloor(nCurrentLevel);
		retrieveNextFloorTileInstruction();
	}
	
	public void updateLevel(){
		tileController.animate();
		
		int currentFloorTile = tileController.getFloorCount();

		if(nCurrentFloorTile != currentFloorTile){
			nCurrentFloorTile = currentFloorTile;
			
			if(nCurrentFloorTile == nTileMarker){
				if(bEndOfLevel){
					tileController.setAnimating(false);
					return;
				}
				else
				{
					nTileChangeCounter = tileController.getNumberOfTiles();
				}
			}
			
			if(nTileChangeCounter > 0){
				tileController.changeTexture(nTextureId);
				nTileChangeCounter--;
				
				if(nTileChangeCounter == 0){
					retrieveNextFloorTileInstruction();
				}
			}
		}
	}
	
	public void setupInstructionsFloor(int level){
		switch (level) {
			case 1:
				sInstructionsFloor = new LinkedList<String>();
				sInstructionsFloor.add("10");
				sInstructionsFloor.add("f");
				sInstructionsFloor.add("c");
				sInstructionsFloor.add("1");
				sInstructionsFloor.add("50");
				sInstructionsFloor.add("f");
				sInstructionsFloor.add("c");
				sInstructionsFloor.add("0");
				sInstructionsFloor.add("100");
				sInstructionsFloor.add("eol");
				break;
			default:
				break;
		}
	}

	private void retrieveNextFloorTileInstruction(){
		try
		{
			if(sInstructionsFloor.size() < 4)
			{
				// No more instructions
				bEndOfLevel = true;
				return;
			}

			nTileMarker = Integer.parseInt(sInstructionsFloor.removeFirst());
			sTargetController = sInstructionsFloor.removeFirst();
			sControllerCommand = sInstructionsFloor.removeFirst();
			nTextureId = Integer.parseInt(sInstructionsFloor.removeFirst());
		}
		catch (NumberFormatException e)
		{
			Log.e("LevelController", "Expected tile number");
		}
	}
	
	public void draw() {
		tileController.draw();
	}
}
