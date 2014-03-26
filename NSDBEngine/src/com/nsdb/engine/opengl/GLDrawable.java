package com.nsdb.engine.opengl;

import javax.microedition.khronos.opengles.GL10;

public interface GLDrawable {
	public void draw(GL10 gl);
	public void load();
	
	public void setColor(float r,float g,float b);
	public void setAlpha(float a);
	public void setLineColor(float r, float g, float b);
	
	public float getWidth();
	public float getHeight();
	public float getCenterX();
	public float getCenterY();
	public float getLineWidth();
	public boolean isLoaded();
}
