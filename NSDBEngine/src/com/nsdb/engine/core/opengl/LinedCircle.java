package com.nsdb.engine.core.opengl;


public class LinedCircle extends Circle {
	
	public LinedCircle(float radius,int polyCount,float lineWidth) {
		super(radius,polyCount);
		// line buffer
		byte[] indices=new byte[polyCount];
		for(int i=0;i<polyCount;i++)
			indices[i]=(byte)(i+1);
		setLine(indices, lineWidth);
	}
}
