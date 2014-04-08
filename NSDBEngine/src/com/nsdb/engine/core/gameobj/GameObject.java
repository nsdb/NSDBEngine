package com.nsdb.engine.core.gameobj;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;

import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.Controllable;
import com.nsdb.engine.core.util.GLDrawable;
import com.nsdb.engine.core.util.GameEvent;
import com.nsdb.engine.core.util.GameLog;

/**
 * 
 * Abstract class of object that can act in this game.<br>
 * for setting root GameObject to GameActivity(GameController), You must use it.<br>
 *
 */
public abstract class GameObject implements Controllable,Communicable {
	
	protected final Communicable con;
	protected PointF point;
	protected GLDrawable drawable;
	
	public GameObject(Communicable con) {
		this.con=con;
		point=new PointF(0,0);
	}
	
	@Override
	public void playGame(int ms) {
		// NOTHING
	}
	
	@Override
	public void receiveMotion(GameEvent ev) {
		// NOTHING
	}
	
	
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
	
	
	@Override
	public void drawScreen(GL10 gl) {
		GLDrawable tDrawable=drawable;
		if(tDrawable==null) return;
		float tax=point.x;
		float tay=point.y;
		
		gl.glTranslatef(-tax, tay, 0);
		tDrawable.draw(gl);
		gl.glTranslatef(tax, -tay, 0);
	}
	
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
