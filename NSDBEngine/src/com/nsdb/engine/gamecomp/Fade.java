package com.nsdb.engine.gamecomp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.core.controller.GC;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.opengl.Rectangle;
import com.nsdb.engine.core.util.Communicable;

public class Fade extends GameObject {
	
	private int type;
	public final static int OUT=10;
	public final static int IN=20;
	
	private int color;
	public final static int BLACK=0;
	public final static int WHITE=1;
	
	private int time;
	private int timeMax;
	
	private FadeEndListener listener;
	
	public Fade(Communicable con,int type,int color,int fadeTime,FadeEndListener listener) {
		super(con);
		this.type=type;
		this.color=color;
		this.time=0;
		this.timeMax=fadeTime;
		this.listener=listener;
		
		drawable=new Rectangle(GC.getGameScreenWidth(),GC.getGameScreenHeight());
		switch(this.color) {
		case BLACK: drawable.setColor(0, 0, 0); break;
		case WHITE: drawable.setColor(1, 1, 1); break;
		}
	}
	
	@Override
	public void playGame(int ms) {
		time+=ms;
		if(time>timeMax && listener!=null)
			listener.fadeEnd(this);
	}
	
	@Override
	public void customDrawSetting(GL10 gl) {
		float alpha=(float)Math.min(time,timeMax)/timeMax;
		if(type==OUT) alpha=1-alpha;
		drawable.setAlpha(alpha);		
	}

	public interface FadeEndListener {
		public void fadeEnd(GameObject obj);
	}

}
