package com.nsdb.engine.core.opengl;


public class LinedRectangle extends Rectangle {

	protected byte[] indices= { 0,1,3,2,0 };
	
	public LinedRectangle(float width, float height, float lineWidth) {
		super(width, height);
		setLine(indices, lineWidth);
	}
}
