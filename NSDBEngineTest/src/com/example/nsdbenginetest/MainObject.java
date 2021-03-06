package com.example.nsdbenginetest;

import com.example.nsdbenginetest.constant.Screen;
import com.example.nsdbenginetest.gameobject.MovingCircle;
import com.example.nsdbenginetest.gameobject.MovingIcon;
import com.example.nsdbenginetest.gameobject.MovingString;
import com.nsdb.engine.core.gameobj.ManagerGameObject;
import com.nsdb.engine.core.opengl.DrawSet;
import com.nsdb.engine.core.opengl.Rectangle;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameEvent;
import com.nsdb.engine.gamecomp.SimpleGameObject;

public class MainObject extends ManagerGameObject {
	
	int testMode;
	SimpleGameObject background;
	
	public MainObject(Communicable con) {
		super(con);
		this.point.x=-Screen.WIDTH/2;
		this.point.y=-Screen.HEIGHT/2;
		testMode=0;
		
		DrawSet bg=new DrawSet();
		Rectangle field=new Rectangle(Screen.WIDTH, Screen.HEIGHT);
		Rectangle field2=new Rectangle(Screen.WIDTH-10, Screen.HEIGHT-10);
		field.setColor(1, 1, 1);
		field2.setColor(0.5f, 0.5f, 0.5f);
		bg.add(field,0,0);
		bg.add(field2,0,0);
		background=new SimpleGameObject(this, Screen.WIDTH/2, Screen.HEIGHT/2, bg);
		
		changeTestMode(testMode);
		
	}

	@Override
	public void customMotion(GameEvent ev) {
		if(ev.getType()==GameEvent.MOTION_CLICK) {
			changeTestMode( (testMode==2)? 0:testMode+1 );
			ev.process();
		}
	}

	private void changeTestMode(int mode) {
		testMode=mode;
		stopControlAll();
		startControl(background);
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
