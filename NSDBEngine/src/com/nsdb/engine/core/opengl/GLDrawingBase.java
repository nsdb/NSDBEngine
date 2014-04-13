package com.nsdb.engine.core.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public abstract class GLDrawingBase implements GLDrawable {
	
	private final static String TAG="GLDrawingBase";
	// buffer
	private FloatBuffer vertexBuffer;
	private int vertexCount;
	private int drawMode;
	// visible status
	private float width,height;
	private float r,g,b,a;
	private float cx,cy;
	// line status
	private ByteBuffer lineIndexBuffer;
	private int lineIndexCount;
	private float lineWidth;
	private float lr,lg,lb;
	
	public GLDrawingBase() {
		vertexBuffer=null;
		vertexCount=0;
		drawMode=0;
		width=0;
		height=0;
		r=1; g=1; b=1; a=1;
		lineIndexBuffer=null;
		lineIndexCount=0;
		lineWidth=0;
		lr=0; lg=0; lb=0;
	}
	
	// status
	protected void setBase(float[] vertices,int drawMode) {
		// buffer, count
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer=vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		vertexCount=vertices.length/3;
		// drawMode
		this.drawMode=drawMode;
		// width, height
		float xmin=vertices[0];
		float xmax=vertices[0];
		float ymin=vertices[1];
		float ymax=vertices[1];
		for(int i=3;i<vertices.length;i+=3) {
			xmin=Math.min(xmin, vertices[i]);
			xmax=Math.max(xmax, vertices[i]);
			ymin=Math.min(ymin, vertices[i+1]);
			ymax=Math.max(ymax, vertices[i+1]);
		}
		width=xmax-xmin;
		height=ymax-ymin;		
		cx=(xmax+xmin)/2;
		cy=(ymax+ymin)/2;
	}
	protected void setLine(byte[] indices, float width) {
		if(indices!=null) {
			lineIndexBuffer=ByteBuffer.allocateDirect(indices.length);
			lineIndexBuffer.put(indices);
			lineIndexBuffer.position(0);
			lineIndexCount=indices.length;
		}
		lineWidth=width;
	}

	// drawing
	@Override
	public void draw(GL10 gl) {
		if(vertexBuffer==null) { Log.i(TAG,"vertexBuffer not set"); return; }
		if(a<=0) return;
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		if(a<1) gl.glEnable(GL10.GL_BLEND);
		else gl.glDisable(GL10.GL_BLEND);
		gl.glLineWidth(lineWidth);
		
		if(drawMode != -1) {
			gl.glColor4f(r, g, b, a);
			gl.glDrawArrays(drawMode, 0, vertexCount);
		}
		if(lineWidth > 0) {
			gl.glColor4f(lr, lg, lb, a);
			if(lineIndexBuffer==null)
				gl.glDrawArrays(GL10.GL_LINE_STRIP, 1, vertexCount);
			else
				gl.glDrawElements(GL10.GL_LINE_STRIP, lineIndexCount, GL10.GL_UNSIGNED_BYTE, lineIndexBuffer);
		}			
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override public void load() {}
	@Override public void setColor(float r, float g, float b) { this.r=r; this.g=g; this.b=b; }
	@Override public void setAlpha(float a) { this.a=a; }
	@Override public void setLineColor(float r, float g, float b) { this.lr=r; this.lg=g; this.lb=b; }
	@Override public float getWidth() { return width; }
	@Override public float getHeight() { return height; }
	@Override public float getCenterX() { return cx; }
	@Override public float getCenterY() { return cy; }
	@Override public float getLineWidth() { return lineWidth; }
	@Override public boolean isLoaded() { return true; }

}
