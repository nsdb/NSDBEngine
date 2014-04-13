package com.nsdb.engine.core.gameobj;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;

import com.nsdb.engine.core.opengl.GLDrawable;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameEvent;
import com.nsdb.engine.core.util.GameLog;

/**
 * 
 * Abstract class of object that can act in this game. MainObject also inherit it.<br>
 * If you want to control other GameObject, use ManagerGameObject
 * @author NSDB
 * @see #con
 * @see #point
 * @see #drawable
 * @see #playGame(int)
 * @see #customMotion(GameEvent)
 * @see #customDrawSetting(GL10)
 * @see #send(int, Object)
 * @see #get(int)
 * @see ManagerGameObject
 *
 */
public abstract class GameObject implements Communicable {
	
	/** Controller of this object. same as parameter of its constructor */
	protected final Communicable con;
	/** Point of this object. relative of its controller. Do not set it to null */
	protected PointF point;
	/** GLDrawable of its object. It will be drawn on its point. */
	protected GLDrawable drawable;
	
	/**
	 * If this object is not MainObject, you can change of its parameter
	 * @param con Controller of this object
	 * @see #con
	 */
	public GameObject(Communicable con) {
		this.con=con;
		point=new PointF(0,0);
	}
	
	/**
	 * Common process of this object. You can override it, but <b>DO NOT CALL THIS METHOD</b>
	 * @param ms period of cycle, millisecond.
	 */
	abstract public void playGame(int ms);
	
	/**
	 * <b>PRIVATE. DO NOT USE THIS METHOD</b>
	 */
	public void receiveMotion(GameEvent ev) {
		if(ev.isProcessed()) return;
		float tax=point.x;
		float tay=point.y;
		
		ev.translate(tax, tay);
		customMotion(ev);
		ev.translate(-tax, -tay);
	}
	
	/**
	 * Touch or key event process of this object. You can override it, but <b>DO NOT CALL THIS METHOD</b>
	 * @param ev Event object. x, y value are relative, same as object point
	 * @see #point
	 */
	protected void customMotion(GameEvent ev) {}
	
//	@Override
//	public void receiveMotion(GameEvent ev) {
//		if(drawable==null) return;
//		float rx=ev.getX()-x;
//		float ry=ev.getY()-y;
//		if(rx<-drawable.getWidth()/2 || rx>drawable.getWidth()/2) return;
//		if(ry<-drawable.getHeight()/2 || ry>drawable.getHeight()/2) return;
//
//		switch(ev.getType()) {
//		case GameEvent.MOTION_CLICK:
//			con.send(EngineID.MSG_CLICKED,this);
//			ev.process();
//			break;
//		case GameEvent.MOTION_SHORTPRESS:
//			con.send(EngineID.MSG_SHORTPRESSED,this);
//			ev.process();
//			break;
//		case GameEvent.MOTION_LONGPRESS:
//			con.send(EngineID.MSG_LONGPRESSED,this);
//			ev.process();
//			break;
//		}
//	}
	
	
	/**
	 * <b>PRIVATE. DO NOT USE THIS METHOD</b>
	 */
	public void drawScreen(GL10 gl) {
		customDrawSetting(gl);
		GLDrawable tDrawable=drawable;
		if(tDrawable==null) return;
		float tax=point.x;
		float tay=point.y;
		
		gl.glTranslatef(-tax, tay, 0);
		tDrawable.draw(gl);
		gl.glTranslatef(tax, -tay, 0);
	}
	
	/**
	 * 'drawable' is drawn automatically on screen. but if you want to change setting of drawable, or OpenGL, such as alpha change, scissoring, override this method.<br>
	 * If you change setting of drawable on other method, it may cause invalid, or unnatural result. (Because thread is different)<br>
	 * <b>DO NOT CALL THIS METHOD, AND DO NOT CALL GLDrawable.draw(GL10)</b>
	 * @param gl OpenGL 1.0 Object. If you don't know about it, just ignore.
	 * @see #drawable
	 */
	protected void customDrawSetting(GL10 gl) {}
	
	@Override
	public int send(int type,Object content) {
		GameLog.danger(this, "Can't understand message : "+type);
		return 0;
	}

	@Override
	public Object get(int name) {
		GameLog.danger(this, "Invalid get parameter : "+name);
		return null;
	}
	
	
	/**
	 *  for remove method of ArrayList<>
	 */
	@Override
	public boolean equals(Object o) {
		return this.hashCode()==o.hashCode();
	}
}
