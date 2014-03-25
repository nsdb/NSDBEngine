package com.nsdb.engine.opengl;

import javax.microedition.khronos.opengles.GL10;

/**
 * Note : It translate Y point in reverse order (Like Android Canvas, not GL) 
 *
 */
public class TransHelper {
	
	float transX,transY;
	
	public TransHelper() {
		transX=0;
		transY=0;
	}
	
	public void setBasePoint(GL10 gl,float x,float y,float width,float height) {
		rollback(gl);
		translate(gl,x-width/2,y-height/2);
	}
	
	public void translate(GL10 gl,float x,float y) {
		transX+=x;
		transY+=y;
		gl.glTranslatef(x, -y, 0);
	}
	
	public void rollback(GL10 gl) {
		translate(gl,-transX,-transY);
	}

}
