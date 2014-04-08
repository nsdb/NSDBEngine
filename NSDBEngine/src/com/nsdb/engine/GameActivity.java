package com.nsdb.engine;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.nsdb.engine.core.controller.GameController;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameLog;

/**
 * Parent class of MainActivity. Your MainActivity must inherit this,<br>
 * and set root GameObject and screen size. Then you can start game.
 *
 */
public class GameActivity extends Activity implements OnTouchListener {

	private GLSurfaceView view;	
	private GameController thread;
	private GameObject main;

	// game ready
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// window setting (no title bar)
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// logger
		GameLog.init("");
		GameLog.info(this,"GameActivity created");

		// surface view
		view=new GLSurfaceView(this);
		view.setEGLConfigChooser(8, 8, 8, 0, 16, 8);
		view.setOnTouchListener(this);
		setContentView(view);
		GameLog.info(this,"GL SurfaceView created");
		
		// thread
		thread=new GameController(this,view);
		
		// no status bar
		setStatusBarVisible(false);
		
	}
	
	// root setting (must)
	protected void setMainObject(Class<? extends GameObject> c) {
		try {
			main=c.getConstructor(new Class[] { Communicable.class }).newInstance(thread);
		} catch (Exception e) {
			GameLog.error(this,"Failed to create MainObject");
			e.printStackTrace();
		}
		thread.setMainObject(main);
	}
	
	// game screen setting (must)
	protected void setGameScreenValue(int width,int height,boolean horizontal) {
		thread.setGameScreenValue(width, height, horizontal);
	}
	
	// window setting
	protected void setStatusBarVisible(boolean visible) {
		if(visible) {
			getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 			
		} else {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 			
		}
	}
	
	// game start
	protected void startGame() {
		thread.start();
	}
	
	// game restart
	@Override
	public void onResume() {
		super.onResume();
		GameLog.info(this,"GameActivity resumed");
		thread.restart();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// game pause
		GameLog.info(this,"GameActivity paused");				
		thread.pause();
		
		// game end
		if(this.isFinishing()) {
			GameLog.info(this,"GameActivity destroyed");
			thread.end();
		}
	}
	
	// touch event
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		thread.pushEvent(event);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_BACK) {
			thread.pushEvent(keyCode);
			return true;
		}
		return false;
	}
	
	
}


