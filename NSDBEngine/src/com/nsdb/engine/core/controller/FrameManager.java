package com.nsdb.engine.core.controller;

import com.nsdb.engine.core.util.GameLog;

/**
 * 
 * Frame manager for GameController, private.
 *
 */
public class FrameManager {
	
	private int period;
	private long lostTime;
	private long fms,lms;
	private boolean measuring;
	protected final static int PERIOD_BASE=15;
	protected final static float PERIOD_SAFE_MIN=0.5f;
	protected final static float PERIOD_SAFE_MAX=0.8f;
	
	public FrameManager() {
		period=PERIOD_BASE;
		lostTime=0;
		measuring=false;
	}
	
	public int getPeriod() { return period; }
	
	public void startMeasure() {
		if(measuring) GameLog.error(this,"Already measuring");
		fms=System.currentTimeMillis();
		measuring=true;
	}
	
	public void endMeasureAndProcess() {
		if(!measuring) { GameLog.error(this,"You didn't start measuring"); return; }
		lms=System.currentTimeMillis();
		measuring=false;
		
		// sleep or loss time check
		long sleepTime=period-lms+fms-lostTime;
		if(sleepTime>=0) {
			try {
				Thread.sleep(sleepTime);
				lostTime=0;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			lostTime=-sleepTime;
			GameLog.debug(this,"Lost time exist : "+lostTime);
		}
		
		// safe frame check
		if(lms-fms > period*PERIOD_SAFE_MAX || lms-fms < period*PERIOD_SAFE_MIN) {
			int afterPeriod=Math.round((lms-fms)/PERIOD_SAFE_MAX);
			if(afterPeriod<PERIOD_BASE) afterPeriod=PERIOD_BASE;
			//GameLog.debug(this,"Frame change : "+period+"->"+afterPeriod);
			period=afterPeriod;
		}
	}

}
