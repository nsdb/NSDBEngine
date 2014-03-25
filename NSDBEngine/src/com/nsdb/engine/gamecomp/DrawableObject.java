package com.nsdb.engine.gamecomp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.core.GameObject;
import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.opengl.TransHelper;
import com.nsdb.engine.util.Communicable;

public class DrawableObject extends GameObject {
	
	protected float x,y;
	protected float gameScreenWidth,gameScreenHeight;
	protected GLDrawable drawable;
	protected int layer;
	protected TransHelper helper;
	
	public DrawableObject(Communicable con, GLDrawable drawable, int layer) {
		this(con,0,0,drawable,layer);
		x=gameScreenWidth/2;
		y=gameScreenHeight/2;
	}

	public DrawableObject(Communicable con, float x, float y, GLDrawable drawable, int layer) {
		super(con);
		this.x=x;
		this.y=y;
		this.gameScreenWidth=(Integer)con.get("gameScreenWidth");
		this.gameScreenHeight=(Integer)con.get("gameScreenHeight");
		this.drawable=drawable;
		if(drawable != null) drawable.load(con);
		this.layer=layer;
		this.helper=new TransHelper();
	}
	
	@Override
	public void drawScreen(GL10 gl,int layer) {
		if(layer != this.layer) return;
		helper.setBasePoint(gl, x, y, gameScreenWidth, gameScreenHeight);
		if(drawable != null)
			drawable.draw(gl);
		helper.rollback(gl);
	}
	
	@Override
	public Object get(String name) {
		if(name.equals("isLoaded"))
			if(drawable==null)
				return true;
			else
				return drawable.isLoaded();
		else if(name.equals("drawable"))
			return drawable;
		else if(name.equals("x"))
			return x;
		else if(name.equals("y"))
			return y;
		else
			return super.get(name);
	}
	
	protected void setDrawable(GLDrawable drawable) {
		this.drawable=drawable;
		drawable.load(con);
	}
	
	protected void setPoint(float x,float y) {
		this.x=x;
		this.y=y;
	}

}
