package com.nsdb.engine.gamecomp;

import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.util.Communicable;

public class SimpleGameObject extends GameObject {
	
	public SimpleGameObject(Communicable con, GLDrawable drawable) {
		this(con,0,0,drawable);
	}

	public SimpleGameObject(Communicable con, float x, float y, GLDrawable drawable) {
		super(con);
		this.point.x=x;
		this.point.y=y;
		this.drawable=drawable;
		if(drawable != null) drawable.load();
	}
	
}
