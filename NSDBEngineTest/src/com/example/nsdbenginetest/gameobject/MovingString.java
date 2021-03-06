package com.example.nsdbenginetest.gameobject;

import com.example.nsdbenginetest.constant.Screen;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.opengl.StringTexture;
import com.nsdb.engine.core.util.Communicable;

public class MovingString extends GameObject {

	private float speedX,speedY;

	public MovingString(Communicable con) {
		super(con);
		point.x=(float)Math.random()*(Screen.WIDTH);
		point.y=(float)Math.random()*(Screen.HEIGHT);
		this.speedX=(float)Math.random()*600-300f;
		this.speedY=(float)Math.random()*600-300f;
		if(Math.random()>0.5)
			drawable=new StringTexture("Hello", 50, StringTexture.Align.CENTER);
		else
			drawable=new StringTexture("그래", 50, StringTexture.Align.CENTER);		
		drawable.setColor((float)Math.random(),(float)Math.random(),(float)Math.random());
		drawable.load();
	}

	@Override
	public void playGame(int ms) {
		point.x+=speedX*ms/1000;
		point.y+=speedY*ms/1000;
		if(point.x<0) { point.x=-point.x; speedX*=-1; }
		else if(point.x>Screen.WIDTH) { point.x=Screen.WIDTH*2-point.x; speedX*=-1; }
		if(point.y<0) { point.y=-point.y; speedY*=-1; }
		else if(point.y>Screen.HEIGHT) { point.y=Screen.HEIGHT*2-point.y; speedY*=-1; }
	}

}
