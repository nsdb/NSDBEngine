package com.nsdb.engine.opengl.comp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.nsdb.engine.core.GC;
import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.util.GameLog;

public class BitmapTexture implements GLDrawable {

	// buffer
	private FloatBuffer vertexBuffer;
	private int vertexCount;
	private FloatBuffer texBuffer;
	private final static float[] texCoords = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};
	// visible status
	private float caseSize;
	private float width,height;
	private float r,g,b,a;
	// bitmap
	private int bitmapID;
	private int[] textureIDs;
	private boolean loaded;
	private LoadingThread thread;
	
	public BitmapTexture(float width,float height,int bitmapID) {
		this.caseSize=Math.max(width, height);
		this.width=width;
		this.height=height;
		this.bitmapID=bitmapID;		
		initElse();		
	}	
	public BitmapTexture(float caseSize,int bitmapID) {
		this.caseSize=caseSize;
		
		// get width and height of bitmap
		BitmapFactory.Options option=new BitmapFactory.Options();
		option.inJustDecodeBounds=true;
		Rect rect=new Rect(-1,-1,-1,-1);
		try {
			InputStream is=GC.getContext().getResources().openRawResource(bitmapID);
			BitmapFactory.decodeStream(is, rect, option);
			is.close();
			//bitmap.recycle(); executed at later
		} catch(IOException e) {
			e.printStackTrace();
		}
		float ratio=(float)option.outWidth/option.outHeight;
		if(ratio>1) {
			width=caseSize;
			height=caseSize/ratio;
		} else {
			width=caseSize*ratio;
			height=caseSize;
		}
		////		
		this.bitmapID=bitmapID;		
		initElse();		
	}
	private void initElse() {
		// create vertex buffer
		float[] vertices={
			-width/2, -height/2, 0,
			width/2, -height/2, 0,
			-width/2, height/2, 0,
			width/2, height/2, 0
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
		// define other value
		setColor(1,1,1);
		setAlpha(1);
		textureIDs=new int[1];
		loaded=false;
		thread=null;
		
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
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);
		gl.glColor4f(r,g,b,a);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertexCount);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	@Override
	public void load() {
		if(thread != null || loaded) return;
		textureIDs[0]=GC.getBitmapTextureID(bitmapID);
		if(textureIDs[0]==0) {
			thread=new LoadingThread();
			thread.start();
		} else {
			loaded=true;
		}
		
	}
	
	@Override public void setColor(float r,float g,float b) { this.r=r; this.g=g; this.b=b; }
	@Override public void setAlpha(float a) { this.a=a; }	
	@Override public void setLineColor(float r, float g, float b) {}
	@Override public float getWidth() { return width; }
	@Override public float getHeight() { return height; }
	@Override public float getCenterX() { return 0; }
	@Override public float getCenterY() { return 0; }
	@Override public float getLineWidth() { return 0; }
	@Override public boolean isLoaded() { return loaded; }
	public float getCaseSize() { return caseSize; }
	public int getBitmapID() { return bitmapID; }
	
	private class LoadingThread extends Thread {
		@Override
		public void run() {
			textureIDs[0]=GC.loadBitmapTexture(bitmapID);
			loaded=true;
		}
	}

	
}
