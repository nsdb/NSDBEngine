package com.example.nsdbenginetest.gameobject;

import javax.microedition.khronos.opengles.GL10;

import com.example.nsdbenginetest.constant.Screen;
import com.nsdb.engine.constant.Layer;
import com.nsdb.engine.gamecomp.GameObject;
import com.nsdb.engine.opengl.comp.LinedCircle;
import com.nsdb.engine.util.Communicable;

public class MovingCircle extends GameObject {

	private float x,y;
	private float speedX,speedY;
	private LinedCircle dw;

	public MovingCircle(Communicable con) {
		super(con);
		this.x=(float)Math.random()*(Screen.WIDTH);
		this.y=(float)Math.random()*(Screen.HEIGHT);
		this.speedX=(float)Math.random()*600-300f;
		this.speedY=(float)Math.random()*600-300f;
		this.dw=new LinedCircle((int)(Math.random()*50)+50,36,5);
		dw.setColor((float)Math.random(), (float)Math.random(), (float)Math.random());
		dw.setAlpha(0.5f);
		dw.setLineColor((float)Math.random(), (float)Math.random(), (float)Math.random());
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
	public void drawScreen(GL10 gl,int layer) {
		if(layer != Layer.CHARACTER) return;
		gl.glLoadIdentity();
		gl.glTranslatef(x-Screen.WIDTH/2, y-Screen.HEIGHT/2, 0);
		dw.draw(gl);
	}
}
