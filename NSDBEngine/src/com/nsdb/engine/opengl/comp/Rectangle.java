package com.nsdb.engine.opengl.comp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.opengl.GLDrawingBase;

public class Rectangle extends GLDrawingBase {

	public Rectangle(float width,float height) {
		// buffer
		float[] vertices={
			-width/2, -height/2, 0,
			width/2, -height/2, 0,
			-width/2, height/2, 0,
			width/2, height/2, 0
		};		
		this.setBase(vertices, GL10.GL_TRIANGLE_STRIP);
	}
	
}
