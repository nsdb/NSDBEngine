package com.nsdb.engine.core.gameobj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameEvent;

/**
 * This GameObject can have and control another one, using startControl(GameObject)<br>
 * (Imagine tree data structure)
 */
public class ManagerGameObject extends GameObject {

	private Queue<InnerMsg> queueGame;
	private Queue<InnerMsg> queueRender;
	private ArrayList<GameObject> childrenGame;
	private ArrayList<GameObject> childrenRender;
	
	public ManagerGameObject(Communicable con) {
		super(con);
		queueGame=new LinkedList<InnerMsg>();
		queueRender=new LinkedList<InnerMsg>();
		childrenGame=new ArrayList<GameObject>();
		childrenRender=new ArrayList<GameObject>();
	}

	/**
	 * Run time of this object.<br>
	 * If you override this, you must call super method for controlling children<br>
	 * Don't call this! (it will be called automatically if you code right)
	 */
	@Override
	public void playGame(int ms) {
		emptyGameQueue();
		for(GameObject o : childrenGame)
			o.playGame(ms);
	}

	/**
	 * Receive touch event, and process.<br>
	 * If you override this, you must call super method, for controlling children<br>
	 * Don't call this! (it will be called automatically if you code right)
	 */
	@Override
	public final void receiveMotion(GameEvent ev) {
		if(ev.isProcessed()) return;
		float tax=point.x;
		float tay=point.y;
		
		ev.translate(tax, tay);
		for(int i=childrenGame.size()-1;i>=0;i--) {
			childrenGame.get(i).receiveMotion(ev);
			if(ev.isProcessed()) break;
		}
		ev.translate(-tax, -tay);
		if(ev.isProcessed()) return;
		
		super.receiveMotion(ev);
	}
	
	/**
	 * Draw screen.<br>
	 * If you override this, you must call super method, for controlling children<br>
	 * Don't call this! (it will be called automatically if you code right)
	 */
	@Override
	public final void drawScreen(GL10 gl) {
		super.drawScreen(gl);
		
		emptyRenderQueue();
		float tax=point.x;
		float tay=point.y;
		
		gl.glTranslatef(-tax, tay, 0);
		for(GameObject o : childrenRender) {
			o.drawScreen(gl);
		}
		gl.glTranslatef(tax, -tay, 0);
	}
	
	/**
	 * Start control child.<br>
	 * Don't call in drawScreen(GL10,int) (It caused thread error)
	 */
	protected void startControl(GameObject... child) {
		InnerMsg msg;
		for(int i=0;i<child.length;i++) {
			msg=new InnerMsg(InnerMsg.TYPE_STARTCON, child[i]);
			queueGame.add(msg);
			synchronized(queueRender) {
				queueRender.add(msg);
			}
		}
	}
	
	/**
	 * Stop control child.<br>
	 * Don't call in drawScreen(GL10,int) (It caused thread error)<br>
	 * Don't start control same GameObject 2 more times.
	 */
	protected void stopControl(GameObject child) {
		InnerMsg msg=new InnerMsg(InnerMsg.TYPE_STOPCON, child);
		queueGame.add(msg);
		synchronized(queueRender) {
			queueRender.add(msg);
		}
	}
	
	/**
	 * Stop control its whole children (including child pushed a moment ago)<br>
	 * Don't call in drawScreen(GL10,int) (It caused thread error)
	 */
	protected void stopControlAll() {
		emptyGameQueue();
		GameObject[] list=(GameObject[])childrenGame.toArray(new GameObject[0]);
		for(int i=0;i<list.length;i++)
			stopControl(list[i]);
	}
	
	
	/**
	 * private
	 */
	private void emptyGameQueue() {
		InnerMsg msg=queueGame.poll();
		while(msg!=null) {
			switch(msg.type){
			
			case InnerMsg.TYPE_STARTCON:
				if(childrenGame.contains((GameObject)msg.content)==false)
					childrenGame.add((GameObject)msg.content);
				break;
			
			case InnerMsg.TYPE_STOPCON:
				childrenGame.remove((GameObject)msg.content);
				break;				
			}
			msg=queueGame.poll();
		}		
	}
	
	/**
	 * private
	 */
	private void emptyRenderQueue() {
		synchronized(queueRender) {
			InnerMsg msg=queueRender.poll();
			while(msg!=null) {
				switch(msg.type){				
				case InnerMsg.TYPE_STARTCON: childrenRender.add((GameObject)msg.content); break;				
				case InnerMsg.TYPE_STOPCON: childrenRender.remove((GameObject)msg.content); break;				
				}
				msg=queueRender.poll();
			}
		}
	}
	
	/**
	 * private
	 */
	private final class InnerMsg {
		public int type;
		public Object content;
		
		public final static int TYPE_STARTCON=1;
		public final static int TYPE_STOPCON=2;
		
		public InnerMsg(int type,Object content) {
			this.type=type;
			this.content=content;
		}
	}	
	
}
