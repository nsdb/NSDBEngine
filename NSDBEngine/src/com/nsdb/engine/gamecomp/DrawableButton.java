package com.nsdb.engine.gamecomp;

import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.GameEvent;

public class DrawableButton extends DrawableObject {
	
	public DrawableButton(Communicable con, float x, float y, GLDrawable drawable, int layer) {
		super(con, x, y, drawable, layer);
	}

	@Override
	public void receiveMotion(GameEvent ev,int layer) {
		if(layer != this.layer) return;
		if(drawable==null) return;
		float rx=ev.getX()-x;
		float ry=ev.getY()-y;
		if(rx<-drawable.getWidth()/2 || rx>drawable.getWidth()/2) return;
		if(ry<-drawable.getHeight()/2 || ry>drawable.getHeight()/2) return;

		switch(ev.getType()) {
		case GameEvent.MOTION_CLICK:
			con.send("clicked",this);
			ev.process();
			break;
		case GameEvent.MOTION_SHORTPRESS:
			con.send("shortPressed",this);
			ev.process();
			break;
		case GameEvent.MOTION_LONGPRESS:
			con.send("longPressed",this);
			ev.process();
			break;
		}
	}
	
}
