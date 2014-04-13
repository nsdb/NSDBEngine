package com.nsdb.engine.core.controller;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.nsdb.engine.core.constant.EngineID;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameEvent;
import com.nsdb.engine.core.util.GameLog;

/**
 * PRIVATE. Controller of this game. It helps whole GameObject be active smoothly
 * @author NSDB
 *
 */
public class GameController extends Thread implements Communicable {
	
	// common
	private Activity activity;
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
	
	
	public GameController(Activity activity,GLSurfaceView view) {
		this.activity=activity;
		this.frameManager=new FrameManager();
		this.touchManager=new TouchManager();
		this.view=view;
		this.renderingManager=new RenderingManager(activity);
		this.soundPool=new SoundPool(7,AudioManager.STREAM_MUSIC,0);
		GameLog.info(this,"GameController created");
		GC.init(this);
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
		GameLog.info(this,"Game thread started");
		view.setRenderer(renderingManager);
		if(main==null) {
			GameLog.error(this,"Root Object has not set!");
		}
	}

	// game playing
	@Override
	public final void run() {
		super.run();
		GameLog.info(this,"Game thread is running");
		
		// temp variable (Frequently changed)
		GameEvent ev=null;
		
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
					main.receiveMotion(ev);
				}
				
				// frame check end
				frameManager.endMeasureAndProcess();
				
			}
			////
			
			// wait
			GameLog.debug(this,"Game thread is waiting being restarted");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			////
		}

		// game end
		GameLog.info(this,"Game thread ended");
		if(mediaPlayer != null)
			mediaPlayer.release();
		soundPool.release();
	}
	
	// game restart
	public final void restart() {
		GameLog.info(this,"Game thread restarted");
		isPaused=false;		
		if(mediaPlayer != null)
			mediaPlayer.start();
	}

	// game pause
	public final void pause() {
		GameLog.info(this,"Game thread paused");
		isPaused=true;
		if(mediaPlayer != null)
			mediaPlayer.pause();
	}

	// game end
	public final void end() {
		GameLog.info(this,"Game thread will be ended");
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
	public int send(int type,Object content) {
		GameLog.debug(this,"Controller received message : "+type);
		switch(type) {
		case EngineID.MSG_PLAYSOUND:
			if(mediaPlayer != null)
				mediaPlayer.release();
			mediaPlayer=MediaPlayer.create(activity,(Integer)content);
			if(mediaPlayer!=null) {
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
				return 1;
			} else return 0;
			
		case EngineID.MSG_RESETSOUND:			
			if(mediaPlayer != null)
				mediaPlayer.reset();
			return 1;
			
		case EngineID.MSG_LOADCHUNK:			
			return soundPool.load(activity,(Integer)content,0);
			
		case EngineID.MSG_UNLOADCHUNK:
			if(soundPool.unload((Integer)content)==true)
				return 1;
			else
				return 0;
			
		case EngineID.MSG_PLAYCHUNK:			
			if( soundPool.play((Integer)content,1f,1f,0,0,1f) != 0 )
				return 1;
			else
				return 0;
			
		case EngineID.MSG_GETBITMAPTEXTUREID:
			return renderingManager.getBitmapTextureID( (Integer)content );
			
		case EngineID.MSG_LOADBITMAPTEXTURE:
			return renderingManager.loadBitmapTexture( (Integer)content );
			
		case EngineID.MSG_GETCHARTEXTUREID:
			return renderingManager.getCharTextureID( (Character)content );
			
		case EngineID.MSG_LOADSTRINGTEXTURE:
			return renderingManager.loadStringTexture( (String)content );
			
		}
		
		
		GameLog.error(this,"Controller couldn't understand message : "+type);
		return 0;
	}
	
	
	// getter
	@Override
	public Object get(int name) {
		switch(name) {
		case EngineID.GET_ACTIVTY:
			return activity;
		case EngineID.GET_GAMESCREENWIDTH:
			return gameScreenWidth;
		case EngineID.GET_GAMESCREENHEIGHT:
			return gameScreenHeight;
		default:
			GameLog.error(this,"Controller received invalid name of get() : "+name);
			return null;			
		}
	}
	
}
