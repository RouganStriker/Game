package com.piesinthesky.game;

import game.engine.Texture;

public class TexturePair {
	private int p_id;
	private Texture p_texture;
	
	public TexturePair(int name) {
		p_id = name;
		p_texture = null;
	}
	
	public int getId(){
		return p_id;
	}
	
	public Texture getTexture(){
		return p_texture;
	}
	
	public void setTexture(Texture texture){
		p_texture = texture;
	}
}