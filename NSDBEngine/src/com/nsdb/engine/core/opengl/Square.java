package com.nsdb.engine.core.opengl;



public class Square extends Rectangle {

	private float size;
	
	public Square(float size) {
		super(size,size);
		this.size=size;
	}
	
	public float getSize() { return size; }
}
