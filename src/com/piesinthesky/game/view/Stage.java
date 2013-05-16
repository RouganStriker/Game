package com.piesinthesky.game.view;

/*
 * Singleton, holds the background and foreground
 */
public class Stage {
	private static Background background;
	private static Foreground foreground;
	private static Stage instance = null;
	
	protected Stage(){
		//Prevent instantiation
	}
	
	public Background getBackground() {
		return background;
	}
	public Foreground getForeground() {
		return foreground;
	}
	
	public static Stage getInstance() {
		if(instance == null) {
			background = new Background();
			foreground = new Foreground();
			instance = new Stage();
		}
		return instance;
	}
}
