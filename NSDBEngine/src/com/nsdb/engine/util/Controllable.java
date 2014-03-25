package com.nsdb.engine.util;

import javax.microedition.khronos.opengles.GL10;


public interface Controllable {

	public void playGame(int ms);
	public void receiveMotion(GameEvent ev,int layer);
	public void drawScreen(GL10 gl,int layer);
}
