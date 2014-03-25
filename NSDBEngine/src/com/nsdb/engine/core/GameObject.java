package com.nsdb.engine.core;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.Controllable;
import com.nsdb.engine.util.GameEvent;

/**
 * 
 * Abstract class of object that can act in this game.<br>
 * for setting root GameObject to GameActivity(GameController), You must use it.<br>
 *
 */
public abstract class GameObject implements Controllable,Communicable {

	protected final Communicable con;
	public GameObject(Communicable con) {
		this.con=con;
		//GameLog.debug("GameObject created : hash-"+this.hashCode());
	}
	
	@Override
	public void playGame(int ms) {
		// NOTHING
	}
	
	@Override
	public void receiveMotion(GameEvent ev,int layer) {
		// NOTHING
	}
	
	@Override
	public void drawScreen(GL10 gl,int layer) {
		// NOTHING
	}
	
	@Override
	public int send(String type,Object content) {
		return con.send(type,content);
	}

	@Override
	public Object get(String name) {
		if(name.equals("isLoaded"))
			return true;
		else
			return con.get(name);
	}
	
	
	/**
	 *  for remove method of ArrayList<>
	 */
	@Override
	public boolean equals(Object o) {
		return this.hashCode()==o.hashCode();
	}
}
