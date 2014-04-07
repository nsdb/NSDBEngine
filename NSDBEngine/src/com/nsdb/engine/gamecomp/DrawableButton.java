package com.nsdb.engine.gamecomp;

import com.nsdb.engine.constant.EngineID;
import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.GameEvent;

public class DrawableButton extends DrawableObject {
	
	public DrawableButton(Communicable con, float x, float y, GLDrawable drawable) {
		super(con, x, y, drawable);
	}

	@Override
	public void receiveMotion(GameEvent ev) {
		if(drawable==null) return;
		float rx=ev.getX()-x;
		float ry=ev.getY()-y;
		if(rx<-drawable.getWidth()/2 || rx>drawable.getWidth()/2) return;
		if(ry<-drawable.getHeight()/2 || ry>drawable.getHeight()/2) return;

		switch(ev.getType()) {
		case GameEvent.MOTION_CLICK:
			con.send(EngineID.MSG_CLICKED,this);
			ev.process();
			break;
		case GameEvent.MOTION_SHORTPRESS:
			con.send(EngineID.MSG_SHORTPRESSED,this);
			ev.process();
			break;
		case GameEvent.MOTION_LONGPRESS:
			con.send(EngineID.MSG_LONGPRESSED,this);
			ev.process();
			break;
		}
	}
	
}
