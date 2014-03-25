package com.nsdb.engine.util;

/**
 * Converted class of MotionEvent. Its point(x,y) was regulated for game screen. Way to use it is similiar.<br>
 * If you process it, call process() for smooth use.
 *
 */
public class GameEvent {
	
	public final static int MOTION_DOWN=0;
	public final static int MOTION_DRAG=1;
	public final static int MOTION_UP=2;
	public final static int MOTION_CLICK=3;
	public final static int MOTION_SHORTPRESS=4;
	public final static int MOTION_LONGPRESS=5;
	public final static int KEY_BACK=6;
	
	private int type;
	private float x,y;
	private boolean processed;
	private float transX,transY;
	
	public GameEvent(int type,float x,float y) {
		this.type=type;
		this.x=x;
		this.y=y;
		this.processed=false;
		this.transX=0;
		this.transY=0;
	}
	public GameEvent(int type) {
		this.type=type;
		this.x=-999;
		this.y=-999;
		this.processed=false;
		this.transX=0;
		this.transY=0;
	}
	
	// camera setting
	public void addCameraPoint(float tx, float ty) {
		this.transX+=tx;
		this.transY+=ty;
	}
	
	// carve that it is processed
	public void process() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		processed=true;
	}
	
	// getter
	public int getType() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		return type;
	}
	public float getX() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		return x+transX;
	}
	public float getY() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		return y+transY;
	}
	public float getOriginalX() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		return x;
	}
	public float getOriginalY() {
		if(processed) {
			GameLog.danger("Event is already processed");
		}
		return y;
	}
	public boolean isProcessed() { return processed; }

}
