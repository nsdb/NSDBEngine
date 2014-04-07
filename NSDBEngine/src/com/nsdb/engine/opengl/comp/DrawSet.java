package com.nsdb.engine.opengl.comp;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.opengl.GLDrawable;
import com.nsdb.engine.opengl.TransHelper;

public class DrawSet implements GLDrawable {
	
	// management
	private ArrayList<DrawPair> list;
	private TransHelper helper;
	// drawing set status
	private float width,height;
	private float cx,cy;
	
	public DrawSet() {
		list=new ArrayList<DrawPair>();
		helper=new TransHelper();
		width=0;
		height=0;
		cx=0;
		cy=0;
	}

	// add drawing (you must not add drawable during rendering)
	public void add(GLDrawable drawable,float rx,float ry) {
		list.add(new DrawPair(drawable,rx,ry));
		// calculate size
		DrawPair dp=list.get(0);
		float xmin=dp.drawable.getCenterX()+dp.rx-dp.drawable.getWidth()/2;
		float xmax=dp.drawable.getCenterX()+dp.rx+dp.drawable.getWidth()/2;
		float ymin=dp.drawable.getCenterY()+dp.ry-dp.drawable.getHeight()/2;
		float ymax=dp.drawable.getCenterY()+dp.ry+dp.drawable.getHeight()/2;
		for(int i=1;i<list.size();i++) {
			dp=list.get(i);
			xmin=Math.min(xmin, dp.drawable.getCenterX()+dp.rx-dp.drawable.getWidth()/2);
			xmax=Math.max(xmax, dp.drawable.getCenterX()+dp.rx+dp.drawable.getWidth()/2);
			ymin=Math.min(ymin, dp.drawable.getCenterY()+dp.ry-dp.drawable.getHeight()/2);
			ymax=Math.max(ymax, dp.drawable.getCenterY()+dp.ry+dp.drawable.getHeight()/2);
		}
		width=xmax-xmin;
		height=ymax-ymin;
		cx=(xmax+xmin)/2;
		cy=(ymax+ymin)/2;
	}

	// will call whole drawing's method
	@Override
	public void draw(GL10 gl) {
		for(DrawPair d : list) {
			helper.translate(gl, d.rx, d.ry);
			d.drawable.draw(gl);
			helper.rollback(gl);
		}
	}
	@Override
	public void load() {
		for(DrawPair d : list)
			d.drawable.load();
	};	
	@Override
	public void setColor(float r,float g,float b) {
		for(DrawPair d : list)
			d.drawable.setColor(r, g, b);		
	}
	@Override
	public void setAlpha(float a) {
		for(DrawPair d : list)
			d.drawable.setAlpha(a);		
	}
	@Override
	public void setLineColor(float r, float g, float b) {
		for(DrawPair d : list)
			d.drawable.setLineColor(r,g,b);		
	}
	
	@Override public float getWidth() { return width; }
	@Override public float getHeight() { return height; }
	@Override public float getCenterX() { return cx; }
	@Override public float getCenterY() { return cy; }
	@Override public float getLineWidth() { return 0; }
	@Override
	public boolean isLoaded() { 
		for(DrawPair d : list)
			if(d.drawable.isLoaded()==false)
				return false;
		return true;
	}	
	public GLDrawable getDrawable(int index) {
		return list.get(index).drawable;
	}
	
	private class DrawPair {
		public GLDrawable drawable;
		public float rx,ry;
		public DrawPair(GLDrawable drawable,float rx,float ry) {
			this.drawable=drawable;
			this.rx=rx;
			this.ry=ry;
		}
	}

}
