package com.nsdb.engine.core.controller;

import java.util.LinkedList;
import java.util.Queue;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.nsdb.engine.core.util.Func;
import com.nsdb.engine.core.util.GameEvent;
import com.nsdb.engine.core.util.GameLog;

/**
 * 
 * PRIVATE. Touch manager for GameController
 *
 */
public class TouchManager {
	
	// view info
	private int viewWidth,viewHeight;
	private int gameWidth,gameHeight;
	private boolean horizontal;
	private float scaleRate, transValue;
	
	// motion event
	private Queue<GameEvent> event;
	private boolean pressed;
	private float pressX,pressY;
	private long pressTime;
	private boolean shortPressChecked,longPressChecked;
	protected final static int SPECIAL_ACTION_RANGE=50;
	protected final static int CLICK_TIME=500;
	protected final static int SHORTPRESS_TIME=300;
	protected final static int LONGPRESS_TIME=1000;	
	
	public TouchManager() {
		viewWidth=0;
		viewHeight=0;
		gameWidth=0;
		gameHeight=0;
		scaleRate=1.0f;
		transValue=0;
		event=new LinkedList<GameEvent>();
		pressed=false;
		shortPressChecked=false;
		longPressChecked=false;
	}
	
	public void setViewScreenInfo(int width,int height) {
		viewWidth=width;
		viewHeight=height;
		calculateRegulator();
	}
	
	public void setGameScreenInfo(int width,int height,boolean horizontal) {
		gameWidth=width;
		gameHeight=height;
		this.horizontal=horizontal;
		calculateRegulator();
	}
	
	private void calculateRegulator() {
		// game screen size check
		if(gameHeight==0 || gameWidth==0) {
			GameLog.error(this,"Game screen value has not set!");
			return;
		}
		// calculate
		if(horizontal) {
			scaleRate=(float)viewHeight/gameHeight;
			transValue=(float)(viewWidth-gameWidth*scaleRate)*0.5f;
		} else {
			scaleRate=(float)viewWidth/gameWidth;
			transValue=(float)(viewHeight-gameHeight*scaleRate)*0.5f;							
		}		
	}
	
	public void pushEvent(MotionEvent ev) {
		float tx=ev.getX()/scaleRate;
		float ty=ev.getY()/scaleRate;
		if(horizontal)	tx-=transValue;
		else			ty-=transValue;			
		
		switch(ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if(tx<0 || tx>gameWidth || ty<0 || ty>gameHeight) return;
			event.add(new GameEvent(GameEvent.MOTION_DOWN,tx,ty));
			GameLog.debug(this,"Action Down : "+tx+", "+ty);
			pressed=true;
			pressX=tx;
			pressY=ty;
			pressTime=System.currentTimeMillis();
			shortPressChecked=false;
			longPressChecked=false;
			break;
			
		case MotionEvent.ACTION_UP:
			event.add(new GameEvent(GameEvent.MOTION_UP,tx,ty));
			GameLog.debug(this,"Action Up : "+tx+", "+ty);
			if(pressed && (System.currentTimeMillis()-pressTime)<CLICK_TIME) {
				event.add(new GameEvent(GameEvent.MOTION_CLICK,tx,ty));
				GameLog.debug(this,"Action Click : "+tx+", "+ty);
			}
			pressed=false;
			break;
			
		case MotionEvent.ACTION_MOVE:
			event.add(new GameEvent(GameEvent.MOTION_DRAG,tx,ty));
			//GameLog.debug("Action Move : "+tx+", "+ty);
			if( pressed && Func.distan(pressX,pressY,tx,ty) > SPECIAL_ACTION_RANGE ) {
				GameLog.debug(this,"Click Range Out : "+(tx-pressX)+", "+(ty-pressY));
				pressed=false;
			}
			break;
		}
		
	}
	
	public void pushEvent(int keyCode) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
			event.add(new GameEvent(GameEvent.KEY_BACK));
	}
	
	public GameEvent peekEvent() {
		pressEventCheck();
		return event.peek();
	}
	
	public GameEvent pollEvent() {
		pressEventCheck();
		return event.poll();
	}
	
	private void pressEventCheck() {
		if(pressed) {
			if(!shortPressChecked && System.currentTimeMillis()-pressTime>=SHORTPRESS_TIME) {
				event.add(new GameEvent(GameEvent.MOTION_SHORTPRESS,pressX,pressY));
				GameLog.debug(this,"Action ShortPress : "+pressX+", "+pressY);
				shortPressChecked=true;
			}
			if(!longPressChecked && System.currentTimeMillis()-pressTime>=LONGPRESS_TIME) {
				event.add(new GameEvent(GameEvent.MOTION_LONGPRESS,pressX,pressY));
				GameLog.debug(this,"Action LongPress : "+pressX+", "+pressY);
				longPressChecked=true;
			}		
		}
	}

}
