package com.piesinthesky.game;

import game.engine.Texture;

public class TexturePair {
	private String p_name;
	private Texture p_texture;
	
	public TexturePair(String name) {
		p_name = name;
		p_texture = null;
	}
	
	public String getName(){
		return p_name;
	}
	
	public Texture getTexture(){
		return p_texture;
	}
	
	public void setTexture(Texture texture){
		p_texture = texture;
	}
}