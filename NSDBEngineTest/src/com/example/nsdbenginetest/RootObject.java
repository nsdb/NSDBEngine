package com.example.nsdbenginetest;

import javax.microedition.khronos.opengles.GL10;

import com.example.nsdbenginetest.constant.Screen;
import com.example.nsdbenginetest.gameobject.MovingCircle;
import com.example.nsdbenginetest.gameobject.MovingIcon;
import com.example.nsdbenginetest.gameobject.MovingString;
import com.nsdb.engine.constant.Layer;
import com.nsdb.engine.core.ManagerGameObject;
import com.nsdb.engine.opengl.comp.Rectangle;
import com.nsdb.engine.util.Communicable;
import com.nsdb.engine.util.GameEvent;

public class RootObject extends ManagerGameObject {
	
	int testMode;
	Rectangle field,field2;
	
	public RootObject(Communicable con) {
		super(con);
		testMode=0;
		changeTestMode(testMode);
		field=new Rectangle(Screen.WIDTH, Screen.HEIGHT);
		field2=new Rectangle(Screen.WIDTH-10, Screen.HEIGHT-10);
		field.setColor(1, 1, 1);
		field2.setColor(0.5f, 0.5f, 0.5f);
	}

	@Override
	public void receiveMotionBeforeChildren(GameEvent ev,int layer) {
		if(!ev.isProcessed() && layer==Layer.BACKGROUND && ev.getType()==GameEvent.MOTION_CLICK) {
			changeTestMode( (testMode==2)? 0:testMode+1 );
			ev.process();
		}
	}

	@Override
	public void drawScreenBeforeChildren(GL10 gl,int layer) {
		if(layer==Layer.BACKGROUND) {
			gl.glLoadIdentity();
			field.draw(gl);
			field2.draw(gl);
		}
	}
	
	private void changeTestMode(int mode) {
		testMode=mode;
		stopControlAll();
		switch(mode) {
		case 0:
			for(int i=0;i<200;i++)
				startControl(new MovingCircle(this));
			break;
		case 1:
			for(int i=0;i<200;i++)
				startControl(new MovingIcon(this));
			break;
		case 2:
			for(int i=0;i<200;i++)
				startControl(new MovingString(this));
			break;
		}
	}

}
