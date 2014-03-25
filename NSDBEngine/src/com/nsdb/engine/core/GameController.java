package com.nsdb.engine.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.nsdb.engine.constant.Layer;
import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.GameEvent;
import com.nsdb.engine.util.GameLog;

/**
 * Controller of this game. It helps whole GameObject be active smoothly, private.
 *
 */
public class GameController extends Thread implements Communicable {
	
	// common
	private Context context;
	private int viewScreenWidth,viewScreenHeight;
	private int gameScreenWidth,gameScreenHeight;
	
	// thread
	private boolean isPaused;
	private boolean isEnded;
	
	// game play
	private FrameManager frameManager;
	private TouchManager touchManager;
	private GameObject main;

	// drawing
	private GLSurfaceView view;
	private RenderingManager renderingManager;
	
	// sound
	private MediaPlayer mediaPlayer;
	private SoundPool soundPool;
	
	
	public GameController(Context context,GLSurfaceView view) {
		this.context=context;
		this.frameManager=new FrameManager();
		this.touchManager=new TouchManager();
		this.view=view;
		this.renderingManager=new RenderingManager(context);
		this.soundPool=new SoundPool(7,AudioManager.STREAM_MUSIC,0);
		GameLog.info("GameController created");
	}
	
	// root setting (must)
	public final void setMainObject(GameObject o) {
		main=o;
		renderingManager.setMainObject(o);
	}

	// game screen setting (must)
	public final void setGameScreenValue(int width,int height,boolean horizontal) {
		gameScreenWidth=width;
		gameScreenHeight=height;
		renderingManager.setGameScreenInfo(width, height, horizontal);
		touchManager.setGameScreenInfo(width, height, horizontal);
	}
	
	// game start
	@Override
	public final void start() {
		super.start();
		GameLog.info("Game thread started");
		view.setRenderer(renderingManager);
		if(main==null) {
			GameLog.fetal("Root Object has not set!");
		}
	}

	// game playing
	@Override
	public final void run() {
		super.run();
		GameLog.info("Game thread is running");
		
		// temp variable (Frequently changed)
		GameEvent ev=null;
		int lc;
		
		while(!isEnded) {
			
			// do thread's work (including draw screen)
			while(!isPaused) {

				// frame check start
				frameManager.startMeasure();
				
				// game play
				main.playGame( frameManager.getPeriod() );
				
				// process events
				while(touchManager.peekEvent()!=null) {
					ev=touchManager.pollEvent();
					for(lc=Layer.SIZE-1;(lc>=0 && ev.isProcessed()==false);lc--) {
						main.receiveMotion(ev,lc);
					}
				}
				
				// frame check end
				frameManager.endMeasureAndProcess();
				
			}
			////
			
			// wait
			GameLog.debug("Game thread is waiting being restarted");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			////
		}

		// game end
		GameLog.info("Game thread ended");
		if(mediaPlayer != null)
			mediaPlayer.release();
		soundPool.release();
	}
	
	// game restart
	public final void restart() {
		GameLog.info("Game thread restarted");
		isPaused=false;		
		if(mediaPlayer != null)
			mediaPlayer.start();
	}

	// game pause
	public final void pause() {
		GameLog.info("Game thread paused");
		isPaused=true;
		if(mediaPlayer != null)
			mediaPlayer.pause();
	}

	// game end
	public final void end() {
		GameLog.info("Game thread will be ended");
		isEnded=true;
	}
	
	// touch event
	public final void pushEvent(MotionEvent ev) {
		if(viewScreenWidth==0 && viewScreenHeight==0) {
			viewScreenWidth=renderingManager.getViewWidth();
			viewScreenHeight=renderingManager.getViewHeight();
			touchManager.setViewScreenInfo(viewScreenWidth, viewScreenHeight);
		}
		touchManager.pushEvent(ev);
	}
	public final void pushEvent(int keyCode) {
		touchManager.pushEvent(keyCode);
	}
		
	// communication
	@Override
	public int send(String type,Object content) {
		GameLog.debug("Controller received message : "+type);
		if(type.equals("playSound")) {
			if(mediaPlayer != null)
				mediaPlayer.release();
			mediaPlayer=MediaPlayer.create(context,(Integer)content);
			if(mediaPlayer!=null) {
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				return 1;
			}
			else return 0;
			
		} else if(type.equals("resetSound")) {			
			if(mediaPlayer != null)
				mediaPlayer.reset();
			return 1;
			
		} else if(type.equals("loadChunk")) {			
			return soundPool.load(context,(Integer)content,0);
			
		} else if(type.equals("unloadChunk")) {
			if(soundPool.unload((Integer)content)==true)
				return 1;
			else
				return 0;
			
		} else if(type.equals("playChunk")) {			
			if( soundPool.play((Integer)content,1f,1f,0,0,1f) != 0 )
				return 1;
			else
				return 0;
			
		} else if(type.equals("getBitmapTextureID")) {
			return renderingManager.getBitmapTextureID( (Integer)content );
			
		} else if(type.equals("loadBitmapTexture")) {
			return renderingManager.loadBitmapTexture( (Integer)content );
			
		} else if(type.equals("getCharTextureID")) {
			return renderingManager.getCharTextureID( (Character)content );
			
		} else if(type.equals("loadStringTexture")) {
			return renderingManager.loadStringTexture( (String)content );
			
		}
		
		
		GameLog.error("Controller couldn't understand message : "+type);
		return 0;
	}
	
	// getter
	@Override
	public Object get(String name) {
		if(name.equals("context"))
			return context;
		else if(name.equals("gameScreenWidth"))
			return gameScreenWidth;
		else if(name.equals("gameScreenHeight"))
			return gameScreenHeight;
		else {
			GameLog.error("Controller received invalid name of get() : "+name);
			return null;			
		}
	}
	
}
