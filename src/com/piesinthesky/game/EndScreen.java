package com.piesinthesky.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EndScreen extends Activity implements OnClickListener{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_end_screen);
			setMenuButtons();
		}
		
		private void setMenuButtons(){
			Button replayBtn = (Button) findViewById(R.id.btn_replay);
			Button menuBtn = (Button) findViewById(R.id.btn_menu);
			
			replayBtn.setOnClickListener(this);
			menuBtn.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			int id = v.getId();
			Intent returnIntent = new Intent();
			
			if (id == R.id.btn_replay) {
				returnIntent.putExtra("restart",true);
			} else if (id == R.id.btn_menu) {
				returnIntent.putExtra("restart",false);
			}
			
			setResult(RESULT_OK,returnIntent);
			finish();
		}
}
