package com.nsdb.engine.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

import com.nsdb.engine.gamecomp.GameObject;
import com.nsdb.engine.util.GameLog;

/**
 * 
 * Rendering manager for GameController, private.
 *
 */
public class RenderingManager implements Renderer {

	private Context context;
	private GameObject main;
	private int viewWidth,viewHeight;
	private int gameWidth,gameHeight;
	private boolean horizontal;
	
	private Queue<Integer> loadingBitmapID;
	private ArrayList<BitmapIDPair> bitmapPairList;
	private class BitmapIDPair {
		public BitmapIDPair(int bitmapID, int textureID) {
			this.bitmapID=bitmapID;
			this.textureID=textureID;
		}
		public int bitmapID;
		public int textureID;
	}
	
	private Queue<Character> loadingChar;
	private ArrayList<CharIDPair> charPairList;
	private class CharIDPair {
		public CharIDPair(char character, int textureID) {
			this.character=character;
			this.textureID=textureID;
		}
		public int character;
		public int textureID;
	}
	private Paint paint;
	
	
	public RenderingManager(Context context) {
		this.context=context;
		main=null;
		viewWidth=0;
		viewHeight=0;
		gameWidth=0;
		gameHeight=0;
		horizontal=false;
		loadingBitmapID=new LinkedList<Integer>();
		bitmapPairList=new ArrayList<BitmapIDPair>();
		loadingChar=new LinkedList<Character>();
		charPairList=new ArrayList<CharIDPair>();
		paint=new Paint();
	}
	
	public void setMainObject(GameObject o) {
		main=o;
	}
	
	public void setGameScreenInfo(int width,int height,boolean horizontal) {
		gameWidth=width;
		gameHeight=height;
		this.horizontal=horizontal;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f,0.0f,0.0f,1.0f);	// color for clearing
		gl.glClearDepthf(1.0f);					// depth for clearing
		
		gl.glEnable(GL10.GL_DEPTH_TEST);		// enable depth-buffer for hidden surface??? removal
		gl.glDepthFunc(GL10.GL_LEQUAL);			// type of depth testing to do???
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);			// nice perspective view???

		gl.glShadeModel(GL10.GL_SMOOTH);		// enable smooth shading for color
		
		gl.glDisable(GL10.GL_DITHER);			// disable dithering for better performance		
		
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		viewWidth=width;
		viewHeight=height;
		
		if(height==0) height=1;					// to prevent divide by zero
		float aspect=(float)width/height;		// aspect ratio
		
		gl.glViewport(0, 0, width, height);		// set the display area

		gl.glMatrixMode(GL10.GL_PROJECTION);	// select projection matrix (write mode?)
		gl.glLoadIdentity();					// reset projection matrix
		GLU.gluPerspective(gl, 90.0f, aspect, 0.1f, 3000.0f);	// use(set) perspective projection
				
		// scissor value setting
		float scaleRate,transValue;
		gl.glEnable(GL10.GL_SCISSOR_TEST);
		if(horizontal) {
			scaleRate=(float)viewHeight/gameHeight;
			transValue=(float)(viewWidth-gameWidth*scaleRate)*0.5f;
			gl.glScissor(Math.round(transValue), 0, Math.round(gameWidth*scaleRate), Math.round(gameHeight*scaleRate));
		} else {
			scaleRate=(float)viewWidth/gameWidth;
			transValue=(float)(viewHeight-gameHeight*scaleRate)*0.5f;
			gl.glScissor(0, Math.round(transValue), Math.round(gameWidth*scaleRate), Math.round(gameHeight*scaleRate));
		}
		
		// custom camera setting
		if(horizontal) {
			gl.glTranslatef(0, 0, -gameHeight/2);
		} else {
			gl.glTranslatef(0, 0, -gameWidth/2/aspect);
		}
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);		// select model-view matrix (read mode?)
		gl.glLoadIdentity();					// reset model-view matrix
		
		// game screen size check
		if(gameWidth==0 && gameHeight==0) {
			GameLog.error(this,"Game screen value has not set!");
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		
		//GameLog.debug("Render Thread is Running");
		
		// load texture once
		synchronized(loadingBitmapID) {
			if(loadingBitmapID.peek()!=null)
				loadBitmap(gl,loadingBitmapID.poll());
		}
		synchronized(loadingChar) {
			while(loadingChar.peek()!=null)
				loadChar(gl,loadingChar.poll());
		}

		// clearing
		gl.glDisable(GL10.GL_SCISSOR_TEST);
		gl.glClear(GL10.GL_STENCIL_BUFFER_BIT | GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_SCISSOR_TEST);
		
		// game object drawing
		gl.glLoadIdentity();
		main.drawScreen(gl);
	}
	
	public int getBitmapTextureID(int bitmapID) {
		
		// find texture ID
		for(int i=0;i<bitmapPairList.size();i++) {
			if(bitmapPairList.get(i).bitmapID==bitmapID)
				return bitmapPairList.get(i).textureID;
		}
		return 0;
		
	}
	
	public int loadBitmapTexture(int bitmapID) {
		
		// request to rendering thread
		synchronized(loadingBitmapID) {
			if(loadingBitmapID.contains(bitmapID)==false)
				loadingBitmapID.add(bitmapID);
		}
		
		// find texture ID repeatedly
		while(true) {
			
			for(int i=0;i<bitmapPairList.size();i++) {
				if(bitmapPairList.get(i).bitmapID==bitmapID)
					return bitmapPairList.get(i).textureID;
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public int getCharTextureID(char character) {
		// find texture ID
		for(int i=0;i<charPairList.size();i++) {
			if(charPairList.get(i).character==character)
				return charPairList.get(i).textureID;
		}
		return 0;
	}
	
	public int loadStringTexture(String str) {		
		
		// request to rendering thread
		synchronized(loadingChar) {
			for(int i=0;i<str.length();i++)
				if(loadingChar.contains(str.charAt(i))==false)
					loadingChar.add(str.charAt(i));
		}
		
		// find whole texture ID of character exists
		int firstCharID=0;
		int i,c;
		while(true) {
			for(c=0;c<str.length();c++) {
				for(i=0;i<charPairList.size();i++) {
					if(charPairList.get(i).character==str.charAt(0))
						firstCharID=charPairList.get(i).textureID;						
					if(charPairList.get(i).character==str.charAt(c))
						break;						
				}
				if(i==charPairList.size()) break;
			}
			if(c==str.length()) return firstCharID;
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void loadBitmap(GL10 gl,int bitmapID) {
		
		int[] textureIDs=new int[1];
		
		// duplicate check
		for(int i=0;i<bitmapPairList.size();i++)
			if(bitmapPairList.get(i).bitmapID==bitmapID)
				return;
		
		// setting
		gl.glGenTextures(1, textureIDs, 0);					 // generate texture ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]); // bind to texture ID
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);	// texture filter? 1
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);		// texture filter? 2
		
		// load and register bitmap
		try {
			InputStream is=context.getResources().openRawResource(bitmapID);
			Bitmap bitmap=BitmapFactory.decodeStream(is);
			is.close();
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
		} catch(IOException e) {
			e.printStackTrace();
			
			// fail
			GameLog.info(this,"Failed to load Bitmap Texture : ("+bitmapID+", "+textureIDs[0]+")");
			bitmapPairList.add(new BitmapIDPair(bitmapID,textureIDs[0]));
			return;
		}
		
		// success
		GameLog.info(this,"Bitmap Texture Loaded : ("+bitmapID+", "+textureIDs[0]+")");
		bitmapPairList.add(new BitmapIDPair(bitmapID,textureIDs[0]));
	}
	
	private void loadChar(GL10 gl,char character) {
		if(character==' ') {
			charPairList.add(new CharIDPair(character,0));
			return;
		}
		
		// duplicate check
		for(int i=0;i<charPairList.size();i++)
			if(charPairList.get(i).character==character)
				return;		
		
		// setting
		int[] textureIDs=new int[1];
		gl.glGenTextures(1, textureIDs, 0);					 // generate texture ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]); // bind to texture ID
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);	// texture filter? 1
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);		// texture filter? 2
		
		// create bitmap for char texture
		Bitmap bitmap=Bitmap.createBitmap(75,75,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		paint.setTextSize(50);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(0xFFFFFFFF);
		canvas.drawText(""+character, 37.5f, 37.5f+50/3f, paint);
		
		// register and recycle bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		GameLog.debug(this,"Char Texture Loaded : ("+character+", "+textureIDs[0]+")");
		charPairList.add(new CharIDPair(character,textureIDs[0]));
	}

	public int getViewWidth() { return viewWidth; }
	public int getViewHeight() { return viewHeight; }

}
