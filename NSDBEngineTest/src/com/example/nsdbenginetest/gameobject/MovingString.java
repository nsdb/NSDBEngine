package com.example.nsdbenginetest.gameobject;

import javax.microedition.khronos.opengles.GL10;

import com.example.nsdbenginetest.constant.Screen;
import com.nsdb.engine.core.constant.Align;
import com.nsdb.engine.core.gameobj.GameObject;
import com.nsdb.engine.core.opengl.StringTexture;
import com.nsdb.engine.core.util.Communicable;

public class MovingString extends GameObject {

	private float x,y;
	private float speedX,speedY;
	private StringTexture dw;

	public MovingString(Communicable con) {
		super(con);
		this.x=(float)Math.random()*(Screen.WIDTH);
		this.y=(float)Math.random()*(Screen.HEIGHT);
		this.speedX=(float)Math.random()*600-300f;
		this.speedY=(float)Math.random()*600-300f;
		if(Math.random()>0.5)
			this.dw=new StringTexture("Hello", 50, Align.CENTER);
		else
			this.dw=new StringTexture("그래", 50, Align.CENTER);		
		dw.setColor((float)Math.random(),(float)Math.random(),(float)Math.random());
		dw.load();
	}

	@Override
	public void playGame(int ms) {
		x+=speedX*ms/1000;
		y+=speedY*ms/1000;
		if(x<0) { x=-x; speedX*=-1; }
		else if(x>Screen.WIDTH) { x=Screen.WIDTH*2-x; speedX*=-1; }
		if(y<0) { y=-y; speedY*=-1; }
		else if(y>Screen.HEIGHT) { y=Screen.HEIGHT*2-y; speedY*=-1; }
	}

	@Override
	public void drawScreen(GL10 gl) {
		gl.glLoadIdentity();
		gl.glTranslatef(x-Screen.WIDTH/2, y-Screen.HEIGHT/2, 0);
		if(dw.isLoaded())
			dw.draw(gl);
	}
}
