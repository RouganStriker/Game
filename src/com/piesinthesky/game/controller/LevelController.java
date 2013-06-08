package com.piesinthesky.game.controller;

import game.engine.Engine;
import game.engine.Float2;
import game.engine.Sprite;

import java.util.LinkedList;

import com.piesinthesky.game.CommandsUtil;
import com.piesinthesky.game.CommandsUtil.CommandType;
import com.piesinthesky.game.CommandsUtil.ControllerType;
import com.piesinthesky.game.Game;

import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

public class LevelController {
	private int nCurrentLevel;
	private int nCurrentFloorTile;
	private LinkedList<Integer> sInstructionsFloor;
	
	private int nTileMarker;
	private Integer controllerCommandParam;
	private ControllerType targetController;
	private CommandType controllerCommand;

	private int nTileChangeCounter;
	private boolean bEndOfLevel;
	private FloorObjController tileController;
	private SurfaceObjController obstacleController;

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
		obstacleController  = new SurfaceObjController(boundary, vel, blockSize.x, blockSize.y);
		setupInstructionsFloor(nCurrentLevel);
		retrieveNextFloorTileInstruction();
	}
	
	public void updateLevel(){
		tileController.animate();
		obstacleController.animate();
		int currentFloorTile = tileController.getFloorCount();

		if(nCurrentFloorTile != currentFloorTile){
			nCurrentFloorTile = currentFloorTile;
			
			if(nCurrentFloorTile == nTileMarker){
				if(bEndOfLevel){
					tileController.setAnimating(false);
					obstacleController.setAnimating(false);
					return;
				}
				else
				{
					nTileChangeCounter = tileController.getNumberOfTiles();
				}
			}
			
			if(nTileChangeCounter > 0){
				tileController.changeTexture(controllerCommandParam.intValue());
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
				sInstructionsFloor = new LinkedList<Integer>();
				sInstructionsFloor.add(10);
				sInstructionsFloor.add(0);
				sInstructionsFloor.add(0);
				sInstructionsFloor.add(1);
				sInstructionsFloor.add(30);
				sInstructionsFloor.add(0);
				sInstructionsFloor.add(0);
				sInstructionsFloor.add(0);
				sInstructionsFloor.add(50);
				sInstructionsFloor.add(2);
				sInstructionsFloor.add(1);
				sInstructionsFloor.add(null);
				break;
			default:
				break;
		}
	}

	private void retrieveNextFloorTileInstruction(){
		if(sInstructionsFloor.size() >= 4)
		{
			nTileMarker = sInstructionsFloor.removeFirst();
			targetController = CommandsUtil.getControllerType(sInstructionsFloor.removeFirst());
			controllerCommand = CommandsUtil.getCommandType(sInstructionsFloor.removeFirst());
			controllerCommandParam = sInstructionsFloor.removeFirst();
			
			if(targetController == ControllerType.LEVEL  && controllerCommand == CommandType.END) {
				if(sInstructionsFloor.size() <= 4)
				{
					bEndOfLevel = true;
				}
			}
		}
	}
	
	public void draw() {
		tileController.draw();
		obstacleController.draw();
	}
	
	public LinkedList<Sprite> getObstacles(){
		return obstacleController.getSpriteList();
	}
	
	public void end(){
		tileController.setAnimating(false);
		obstacleController.setAnimating(false);
	}
}
