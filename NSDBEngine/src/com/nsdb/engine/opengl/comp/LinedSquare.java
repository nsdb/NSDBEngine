package com.nsdb.engine.opengl.comp;


public class LinedSquare extends LinedRectangle {

	private float size;
	
	public LinedSquare(float size, float lineWidth) {
		super(size, size, lineWidth);
		this.size=size;
	}
	
	public float getSize() { return size; }

}
