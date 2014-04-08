package com.nsdb.engine.gamecomp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.core.constant.EngineID;
import com.nsdb.engine.core.controller.GC;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameEvent;

public class Pager extends GameObject {
	
	private int direction;
	public final static int RIGHT=10;
	public final static int LEFT=20;
	
	private int time;
	private int timeMax;
	private int gameScreenWidth;
	
	private GameObject previousPage;
	private GameObject nextPage;

	public Pager(Communicable con,int direction,int time,GameObject previous,GameObject next) {
		super(con);
		this.direction=direction;
		this.time=0;
		this.timeMax=time;
		this.previousPage=previous;
		this.nextPage=next;
		this.gameScreenWidth=GC.getGameScreenWidth();
		
	}
	
	@Override
	public void playGame(int ms) {
		time+=ms;
		if(time>timeMax) con.send(EngineID.MSG_PAGEREND,this);
	}
	
	@Override
	public void receiveMotion(GameEvent ev) {
		if(ev.isProcessed()==false) ev.process();
	}
	
	@Override
	public void drawScreen(GL10 gl) {
		double bPer=(double)Math.min(time,timeMax)/timeMax;
		double pPer=Math.pow( 1-Math.pow(1-bPer,2), 0.5 );
		float pValue=(float)(-gameScreenWidth*pPer);
		float dValue=(direction==RIGHT)? 1:-1;
		gl.glTranslatef(pValue*dValue, 0, 0);
		previousPage.drawScreen(gl);
		gl.glTranslatef(gameScreenWidth*dValue, 0, 0);
		nextPage.drawScreen(gl);				
		gl.glTranslatef( (-pValue-gameScreenWidth)*dValue, 0, 0);
		
	}
}
