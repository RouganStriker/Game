package com.piesinthesky.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_game);
		setMenuButtons();
	}
	
	private void setMenuButtons(){
		Button playBtn = (Button) findViewById(R.id.btn_play);
		Button tutorialBtn = (Button) findViewById(R.id.btn_tut);
		Button settingsBtn = (Button) findViewById(R.id.btn_settings);
		Button exitBtn = (Button) findViewById(R.id.btn_exit);
		Button closeBtn = (Button) findViewById(R.id.btn_closePopup);
		
		playBtn.setOnClickListener(this);
		tutorialBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);
	}

	private enum MenuType{
		PLAY,
		TUTORIAL,
		SETTINGS,
		EXIT;
	}
	
	private void openPopup(MenuType menuType){
		findViewById(R.id.greyOverlay).setVisibility(View.VISIBLE);
		
		if(menuType == MenuType.SETTINGS)
			findViewById(R.id.btn_savePopup).setVisibility(View.VISIBLE);
	}
	
	private void closePopup(){
		findViewById(R.id.greyOverlay).setVisibility(View.GONE);
		findViewById(R.id.btn_savePopup).setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.btn_play) {
			Intent game = new Intent(this, Game.class);
			startActivity(game);
		} else if (id == R.id.btn_tut) {
			openPopup(MenuType.TUTORIAL);
		} else if (id == R.id.btn_settings) {
			openPopup(MenuType.SETTINGS);
		} else if (id == R.id.btn_exit) {
			finish();
		} else if (id == R.id.btn_closePopup) {
			closePopup();
		}
	}

}
