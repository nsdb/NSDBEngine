package com.nsdb.engine.core.opengl;

import javax.microedition.khronos.opengles.GL10;


public class GLHelper {

	public static void startStencil(GL10 gl,GLDrawable mask) {
		gl.glClear(GL10.GL_STENCIL_BUFFER_BIT);
		gl.glEnable(GL10.GL_STENCIL_TEST);
		gl.glStencilFunc(GL10.GL_NEVER, 0, 0);
		gl.glStencilOp(GL10.GL_INCR, GL10.GL_INCR, GL10.GL_INCR);
		mask.draw(gl);
		gl.glStencilFunc(GL10.GL_EQUAL, 1, 1);
		gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);		
	}
	
	public static void endStencil(GL10 gl) {
		gl.glDisable(GL10.GL_STENCIL_TEST);		
	}
}
