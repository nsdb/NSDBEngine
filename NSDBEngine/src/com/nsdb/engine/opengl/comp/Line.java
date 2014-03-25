package com.nsdb.engine.opengl.comp;

import com.nsdb.engine.opengl.GLDrawingBase;

import android.graphics.PointF;

public class Line extends GLDrawingBase {

	private PointF[] points;
	
	public Line(float lineWidth, PointF... points) {
		// To query the range of supported widths, call glGet with arguments GL_ALIASED_LINE_WIDTH_RANGE or GL_SMOOTH_LINE_WIDTH_RANGE.
		this.points=points;
		// buffer
		float[] vertices=new float[points.length*3];
		for(int i=0;i<points.length;i++) {
			vertices[i*3]=points[i].x;
			vertices[i*3+1]=points[i].y;
			vertices[i*3+2]=0;
		}
		setBase(vertices,-1);
		setLine(null, lineWidth);
	}
	public PointF getPoint(int index) { return points[index]; }
}
