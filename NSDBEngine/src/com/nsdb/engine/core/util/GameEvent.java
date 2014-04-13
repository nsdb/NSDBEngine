package com.nsdb.engine.core.util;

/**
 * Converted class of MotionEvent. Its point(x,y) was regulated for game screen.<br>
 * If you process it, call process() for smooth use.
 * @author NSDB
 * @see #process()
 * @see #getType()
 * @see #getX()
 * @see #getY()
 * @see #getOriginalX()
 * @see #getOriginalY()
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
	
	public GameEvent(int type) {
		this(type,-999,-999);
	}
	public GameEvent(int type,float x,float y) {
		this.type=type;
		this.x=x;
		this.y=y;
		this.processed=false;
		this.transX=0;
		this.transY=0;
	}
	
	
	/**
	 * PRIVATE
	 */
	public void translate(float tx, float ty) {
		this.transX+=tx;
		this.transY+=ty;
	}
	
	// 
	/**
	 * carve that it is processed. If you don't call this method after processing, other GameObject will use this event repeatedly.
	 */
	public void process() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		processed=true;
	}
	
	/**
	 * @return type of event. such as MOTION_DOWN
	 */
	public int getType() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		return type;
	}
	public float getX() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		return x+transX;
	}
	public float getY() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		return y+transY;
	}
	public float getOriginalX() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		return x;
	}
	public float getOriginalY() {
		if(processed) {
			GameLog.danger(this,"Event is already processed");
		}
		return y;
	}
	public boolean isProcessed() { return processed; }

}
