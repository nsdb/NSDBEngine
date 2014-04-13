package com.example.nsdbenginetest;

import android.os.Bundle;

import com.example.nsdbenginetest.constant.Screen;
import com.nsdb.engine.GameActivity;

public class MainActivity extends GameActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMainObject(MainObject.class);
		setGameScreenValue(Screen.WIDTH,Screen.HEIGHT,Screen.HORIZONTAL);
		setStatusBarVisible(true);
		startGame();
	}

}
