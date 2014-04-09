package com.nsdb.engine.core.opengl;

import javax.microedition.khronos.opengles.GL10;

import com.nsdb.engine.core.util.GameLog;

public class VariableStringTexture extends StringTexture {
	
	private String printString;

	public VariableStringTexture(String baseString, float fontSize, Align align) {
		super(baseString, fontSize, align);
		textureWidth=0;
	}
	
	@Override
	public void draw(GL10 gl) {
		if(!loaded) { GameLog.debug(this,"Texture is not loaded"); return; }
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		
		// set rendering start point
		switch(align) {
		case LEFT: default: break;
		case CENTER: helper.translate(gl, -textureWidth/2, 0); break;
		case RIGHT: helper.translate(gl, -textureWidth, 0); break;
		}
		helper.translate(gl, fontSize/4, 0);
		
		// rendering
		gl.glColor4f(r,g,b,a);
		for(int i=0;i<printLength;i++) {

			helper.translate(gl, getCharSize(printString.charAt(i))/2, 0);
			for(int j=0;j<string.length();j++) {
				if(string.charAt(j)==printString.charAt(i)) {
					if(textureIDs[j]!=0) {
						gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[j]);			
						gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertexCount);
					}
					break;
				}
			}
			helper.translate(gl, getCharSize(printString.charAt(i))/2, 0);
		}
		
		// recover point
		helper.rollback(gl);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	public void setPrintString(String printString) {
		this.printString=printString;
		textureWidth=fontSize/4;
		for(int i=0;i<printString.length();i++)
			textureWidth+=getCharSize(printString.charAt(i));
		textureWidth+=fontSize/4;
		setPrintLength(printString.length());
	}
	
	@Override
	public void setPrintLength(int length) {
		if(printString==null)
			printLength=0;
		else if(length>printString.length() || length<0)
			printLength=printString.length();
		else
			printLength=length;		
	}

}
