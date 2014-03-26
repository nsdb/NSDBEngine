package com.nsdb.engine.gamecomp;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.constant.EngineID;
import com.nsdb.engine.core.GC;
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
		this.gameScreenWidth=GC.getGameScreenWidth();
		this.gameScreenHeight=GC.getGameScreenHeight();
		this.drawable=drawable;
		if(drawable != null) drawable.load();
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
	public Object get(int name) {
		switch(name) {
		case EngineID.GET_ISLOADED:
			if(drawable==null)
				return true;
			else
				return drawable.isLoaded();
		case EngineID.GET_DRAWABLE:
			return drawable;
		case EngineID.GET_X:
			return x;
		case EngineID.GET_Y:
			return y;
		default:
			return super.get(name);
		}
	}
	
	protected void setDrawable(GLDrawable drawable) {
		this.drawable=drawable;
		drawable.load();
	}
	
	protected void setPoint(float x,float y) {
		this.x=x;
		this.y=y;
	}

}
