package com.nsdb.engine.core.opengl;


public class LinedCircle extends Circle {
	
	public LinedCircle(float radius,int polyCount,float lineWidth) {
		super(radius,polyCount);
		// line buffer
		byte[] indices=new byte[polyCount+1];
		for(int i=0;i<polyCount;i++)
			indices[i]=(byte)(i+1);
		indices[polyCount]=indices[0];
		setLine(indices, lineWidth);
	}
}
