package com.nsdb.engine.gamecomp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.constant.EngineID;
import com.nsdb.engine.constant.Layer;
import com.nsdb.engine.core.GC;
import com.nsdb.engine.opengl.comp.Rectangle;
import com.nsdb.engine.util.Communicable;

public class Fade extends GameObject {
	
	private int type;
	public final static int OUT=10;
	public final static int IN=20;
	
	private int color;
	public final static int BLACK=0;
	public final static int WHITE=1;
	
	private int time;
	private int timeMax;
	
	private Rectangle bg;
	
	public Fade(Communicable con,int type,int color,int fadeTime) {
		super(con);
		this.type=type;
		this.color=color;
		this.time=0;
		this.timeMax=fadeTime;
		int gameScreenWidth=GC.getGameScreenWidth();
		int gameScreenHeight=GC.getGameScreenHeight();
		bg=new Rectangle(gameScreenWidth,gameScreenHeight);
		switch(this.color) {
		case BLACK: bg.setColor(0, 0, 0); break;
		case WHITE: bg.setColor(1, 1, 1); break;
		}
	}
	
	@Override
	public void playGame(int ms) {
		time+=ms;
		if(time>timeMax) con.send(EngineID.MSG_FADEEND,this);
	}
	
	@Override
	public void drawScreen(GL10 gl,int layer) {
		if(layer != Layer.SCREEN) return;
		
		float alpha=(float)Math.min(time,timeMax)/timeMax;
		if(type==OUT) alpha=1-alpha;
		bg.setAlpha(alpha);		
		bg.draw(gl);
	}

}
