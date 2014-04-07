package com.nsdb.engine.gamecomp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;

import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.GameEvent;

/**
 * This GameObject can have and control another one, using startControl(GameObject)<br>
 * (Imagine tree data structure)
 */
public class ManagerGameObject extends GameObject {

	private Queue<InnerMsg> queueGame;
	private Queue<InnerMsg> queueRender;
	private ArrayList<GameObject> childrenGame;
	private ArrayList<GameObject> childrenRender;
	private PointF transAllGame;
	private PointF transAllRender;
	private boolean eventMasked;
	
	public ManagerGameObject(Communicable con) {
		super(con);
		queueGame=new LinkedList<InnerMsg>();
		queueRender=new LinkedList<InnerMsg>();
		childrenGame=new ArrayList<GameObject>();
		childrenRender=new ArrayList<GameObject>();
		transAllGame=new PointF(0,0);
		transAllRender=new PointF(0,0);
		eventMasked=false;
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
		float tax=transAllGame.x;
		float tay=transAllGame.y;
		
		eventMasked=false;
		receiveMotionBeforeChildren(ev);
		if(ev.isProcessed()) return;
		if(!eventMasked) {
			ev.addCameraPoint(tax, tay);
			for(int i=childrenGame.size()-1;i>=0;i--) {
				childrenGame.get(i).receiveMotion(ev);
				if(ev.isProcessed()) break;
			}
			ev.addCameraPoint(-tax, -tay);
			if(ev.isProcessed()) return;
		}
		receiveMotionAfterChildren(ev);
	}
	
	protected void receiveMotionBeforeChildren(GameEvent ev) {}
	protected void receiveMotionAfterChildren(GameEvent ev) {}
	protected final void maskEvent() { eventMasked=true; }
	
	/**
	 * Draw screen.<br>
	 * If you override this, you must call super method, for controlling children<br>
	 * Don't call this! (it will be called automatically if you code right)
	 */
	@Override
	public final void drawScreen(GL10 gl) {
		emptyRenderQueue();
		transAllRender.set(transAllGame);
		float tax=transAllRender.x;
		float tay=transAllRender.y;
		
		drawScreenBeforeChildren(gl);
		gl.glTranslatef(-tax, tay, 0);
		for(GameObject o : childrenRender) {
			o.drawScreen(gl);
		}
		gl.glTranslatef(tax, -tay, 0);
		drawScreenAfterChildren(gl);
	}
	
	protected void drawScreenBeforeChildren(GL10 gl) {}
	protected void drawScreenAfterChildren(GL10 gl) {}
	
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
	 * set translate value for children 
	 */
	protected void setTransAll(float x,float y) {
		transAllGame.set(x, y);
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
