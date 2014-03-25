package com.nsdb.engine.opengl.comp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.opengl.GLDrawingBase;

public class Circle extends GLDrawingBase {

	private float radius;
	
	public Circle(float radius,int polyCount) {
		this.radius=radius;
		// buffer
		float[] vertices=new float[(polyCount+2)*3];
		vertices[0]=0; vertices[1]=0; vertices[2]=0;
		double rad;
		for(int i=0 ; i<polyCount+1 ; i++) {
			rad=(double)i/polyCount*Math.PI*2;
			vertices[3+i*3]=(float)Math.cos(rad)*radius;
			vertices[4+i*3]=(float)Math.sin(rad)*radius;
			vertices[5+i*3]=0;
		}
		setBase(vertices,GL10.GL_TRIANGLE_FAN);		
	}
	
	public float getRadius() { return radius; }
}
