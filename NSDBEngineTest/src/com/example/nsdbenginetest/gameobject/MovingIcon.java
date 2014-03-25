package com.example.nsdbenginetest.gameobject;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.example.nsdbenginetest.R;
import com.example.nsdbenginetest.constant.Screen;
import com.nsdb.engine.constant.Layer;
import com.nsdb.engine.core.GameObject;
import com.nsdb.engine.opengl.comp.BitmapTexture;
import com.nsdb.engine.util.Communicable;

public class MovingIcon extends GameObject {

	private float x,y;
	private float speedX,speedY;
	private BitmapTexture dw;

	public MovingIcon(Communicable con) {
		super(con);
		this.x=(float)Math.random()*(Screen.WIDTH);
		this.y=(float)Math.random()*(Screen.HEIGHT);
		this.speedX=(float)Math.random()*600-300f;
		this.speedY=(float)Math.random()*600-300f;
		Context context=(Context)con.get("context");
		this.dw=new BitmapTexture(context,(int)(Math.random()*50)+50,R.drawable.ic_launcher);
		dw.setColor(1,1,1);
		dw.load(con);
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
		if(dw.isLoaded())
			dw.draw(gl);
	}
}
