package com.piesinthesky.game;

public class CommandsUtil {
	public enum ControllerType{
		TILE, OBSTACLE, LEVEL, PLAYER;
	}
	
	public enum CommandType{
		CHANGE_TEXTURE, END;
	}
	
	public static ControllerType getControllerType(int type) {
		switch(type){
			case 0:
				return ControllerType.TILE;
			case 1:
				return ControllerType.OBSTACLE;
			case 2:
				return ControllerType.LEVEL;
			case 3:
				return ControllerType.PLAYER;
			default:
				return null;
		}
	}
	
	public static CommandType getCommandType(int type) {
		switch(type){
			case 0:
				return CommandType.CHANGE_TEXTURE;
			case 1:
				return CommandType.END;
			default:
				return null;
		}
	}
}
