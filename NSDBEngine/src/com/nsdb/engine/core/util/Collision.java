package com.nsdb.engine.core.util;

import com.nsdb.engine.core.util.Func;

public class Collision {

	public final static int TYPE_NULL = 0;
	public final static int TYPE_RECT = 1;
	public final static int TYPE_OVAL = 2;
	
	public int type;
	public float x;
	public float y;
	public float w;
	public float h;
	
	public Collision(int type, float x, float y, float w, float h) {
		this.type = type;
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}

	public boolean checkCollision(Collision col) {
		if(col.type == type) {
			
			switch(type) {
			case TYPE_RECT: return (x+w/2 > col.x-col.w/2 || x-w/2 < col.x+col.w/2) && (y+h/2 > col.y-col.h/2 || y-h/2 < col.y+col.h/2);
			case TYPE_OVAL: return ( Math.pow(x-col.x, 2) + Math.pow(y-col.y, 2) ) < ( Func.distan(w/2*x, h/2*y, w/2*col.x, h/2*col.y) + Func.distan(col.w/2*x, col.h/2*y, col.w/2*col.x, col.h/2*col.y) );
			default: return false;
			}
			
		} else {
			
			Collision rect, oval;
			if(type == TYPE_RECT && col.type == TYPE_OVAL) { rect = this; oval = col; }
			else if(type == TYPE_OVAL && col.type == TYPE_RECT) { rect = col; oval = this; }
			else return false;
			
			// 2 vertical edge of rect and horizontal line of oval
			if( (rect.x+rect.w/2 > oval.x-oval.w/2 || rect.x-rect.w/2 < oval.x+oval.w/2) && (rect.y+rect.h/2 > oval.y && rect.y-rect.h/2 < oval.y) ) return true;
			
			// 2 horizontal edge of rect and vertical line of oval
			if( (rect.x+rect.w/2 > oval.x && rect.x-rect.w/2 < oval.x) && (rect.y+rect.h/2 > oval.y-oval.h/2 || rect.y-rect.h/2 < oval.y+oval.h/2) ) return true;
			
			// 4 vertex of rect and oval
			float rectX = rect.x+rect.w/2;
			float rectY = rect.y+rect.h/2;
			if( ( Math.pow(rectX-oval.x, 2) + Math.pow(rectY-oval.y, 2) ) < Func.distan(oval.w/2*oval.x, oval.h/2*oval.y, oval.w/2*rectX, oval.h/2*rectY) ) return true;
			rectX = rect.x-rect.w/2;
			if( ( Math.pow(rectX-oval.x, 2) + Math.pow(rectY-oval.y, 2) ) < Func.distan(oval.w/2*oval.x, oval.h/2*oval.y, oval.w/2*rectX, oval.h/2*rectY) ) return true;
			rectY = rect.y-rect.h/2;
			if( ( Math.pow(rectX-oval.x, 2) + Math.pow(rectY-oval.y, 2) ) < Func.distan(oval.w/2*oval.x, oval.h/2*oval.y, oval.w/2*rectX, oval.h/2*rectY) ) return true;
			rectX = rect.x+rect.w/2;
			if( ( Math.pow(rectX-oval.x, 2) + Math.pow(rectY-oval.y, 2) ) < Func.distan(oval.w/2*oval.x, oval.h/2*oval.y, oval.w/2*rectX, oval.h/2*rectY) ) return true;
			
			return false;
		}
		
	}
}
