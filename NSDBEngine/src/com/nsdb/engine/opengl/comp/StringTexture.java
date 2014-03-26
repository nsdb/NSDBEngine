package com.nsdb.engine.opengl.comp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.constant.Align;
import com.nsdb.engine.core.GC;
import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.opengl.TransHelper;
import com.nsdb.engine.util.GameLog;

public class StringTexture implements GLDrawable {

	// buffer
	protected FloatBuffer vertexBuffer;
	protected int vertexCount;
	protected FloatBuffer texBuffer;
	private float[] texCoords = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};
	// visible status
	protected String string;
	protected float fontSize;
	protected float textureWidth;
	protected int align;
	protected float r,g,b,a;
	protected int printLength;
	// bitmap
	protected int[] textureIDs;
	protected boolean loaded;
	private LoadingThread thread;
	protected TransHelper helper;
	
	
	public StringTexture(String string, float fontSize, int align) {
		this.string=string;
		this.fontSize=fontSize;
		
		// create vertex buffer
		float[] vertices={
			-fontSize/2*1.5f, -fontSize/2*1.5f, 0,
			fontSize/2*1.5f, -fontSize/2*1.5f, 0,
			-fontSize/2*1.5f, fontSize/2*1.5f, 0,
			fontSize/2*1.5f, fontSize/2*1.5f, 0
		};
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer=vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);		
		vertexCount=vertices.length/3;
		// create texture coord buffer
		ByteBuffer tbb=ByteBuffer.allocateDirect(texCoords.length*4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer=tbb.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);		
		// calculate string texture width
		textureWidth=fontSize/4;
		for(int i=0;i<string.length();i++)
			textureWidth+=getCharSize(string.charAt(i));
		textureWidth+=fontSize/4;
		// define other value
		this.align=align;
		setColor(1,1,1);
		setAlpha(1);
		setPrintLength(string.length());
		textureIDs=new int[string.length()];
		loaded=false;
		thread=null;
		helper=new TransHelper();
	}
	
	@Override 
	public void draw(GL10 gl) {
		if(!loaded) { GameLog.debug(this,"Texture is not loaded"); return; }
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		
		// set rendering start point
		switch(align) {
		case Align.LEFT: default: break;
		case Align.CENTER: helper.translate(gl, -textureWidth/2, 0); break;
		case Align.RIGHT: helper.translate(gl, -textureWidth, 0); break;
		}
		helper.translate(gl, fontSize/4, 0);
		
		// rendering
		gl.glColor4f(r,g,b,a);
		for(int i=0;i<printLength;i++) {

			helper.translate(gl, getCharSize(string.charAt(i))/2, 0);
			if(textureIDs[i]!=0) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[i]);			
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertexCount);
			}
			helper.translate(gl, getCharSize(string.charAt(i))/2, 0);
		}
		
		// recover point
		helper.rollback(gl);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	@Override
	public void load() {
		if(thread != null || loaded) return;
		for(int i=0;i<string.length();i++) {
			textureIDs[i]=GC.getCharTextureID(string.charAt(i));
			if(textureIDs[i]==0 && string.charAt(i)!=' ') {
				thread=new LoadingThread();
				thread.start();				
				return;
			}
		}
		loaded=true;
	}
	
	@Override public void setColor(float r,float g,float b) { this.r=r; this.g=g; this.b=b; }
	@Override public void setAlpha(float a) { this.a=a; }		
	@Override public void setLineColor(float r, float g, float b) {}
	public void setPrintLength(int length) {
		if(length>string.length() || length<0)
			printLength=string.length();
		else
			printLength=length;
	}
	
	@Override public float getWidth() { return textureWidth; }
	@Override public float getHeight() { return fontSize*1.5f; }
	@Override public float getCenterX() { 
		switch(align) {
		case Align.LEFT: return textureWidth/2;
		case Align.CENTER: default: return 0;
		case Align.RIGHT: return -textureWidth/2;
		}
	}
	@Override public float getCenterY() { return 0; }
	@Override public float getLineWidth() { return 0; }
	@Override public boolean isLoaded() { return loaded; }
	public String getString() { return string; }
	public float getFontSize() { return fontSize; }
	
	protected float getCharSize(char c) {
		if(c<=0xFF)
			return fontSize/2;
		else
			return fontSize;
	}	
	private class LoadingThread extends Thread {
		@Override
		public void run() {
			textureIDs[0]=GC.loadStringTexture(string);
			for(int i=1;i<string.length();i++)
				textureIDs[i]=GC.getCharTextureID(string.charAt(i));
			loaded=true;
		}
	}
	
	
}
