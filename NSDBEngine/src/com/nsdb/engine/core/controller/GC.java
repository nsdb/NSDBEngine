package com.nsdb.engine.core.controller;

import android.app.Activity;
import android.content.Context;

import com.nsdb.engine.core.constant.EngineID;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameLog;

/**
 * If you want common action, (such as playing music, get context) use this class.
 * @author NSDB
 *
 */
public class GC {

	private static Communicable con;
	
	public static void init(Communicable con) {
		if(GC.con!=null) {
			GameLog.error("GameControllerProxy","GameController already inited");
			return;
		}
		GC.con=con;
	}
	
	
	public static boolean playSound(int resID) {
		int result=con.send(EngineID.MSG_PLAYSOUND, resID);
		return result==1;
	}
	public static boolean resetSound() {
		int result=con.send(EngineID.MSG_RESETSOUND, null);
		return result==1;
	}
	/**
	 * @return chunkID
	 * @see #playChunk(int)
	 * @see #unloadChunk(int)
	 */
	public static int loadChunk(int resID) {
		int result=con.send(EngineID.MSG_LOADCHUNK, resID);
		return result;
	}
	/**
	 * @see #loadChunk(int)
	 */
	public static boolean unloadChunk(int chunkID) {
		int result=con.send(EngineID.MSG_UNLOADCHUNK, chunkID);
		return result==1;
	}
	/**
	 * @see #loadChunk(int)
	 */
	public static boolean playChunk(int chunkID) {
		int result=con.send(EngineID.MSG_PLAYCHUNK, chunkID);
		return result==1;
	}
	
	/**
	 * PRIVATE
	 */
	public static int getBitmapTextureID(int resID) {
		int result=con.send(EngineID.MSG_GETBITMAPTEXTUREID,resID);
		return result;
	}
	/**
	 * Load bitmap texture, if you load bitmap before game, use this method.
	 * @param resID
	 * @return BitmapID OpenGL use it, but you don't need
	 */
	public static int loadBitmapTexture(int resID) {
		int result=con.send(EngineID.MSG_LOADBITMAPTEXTURE,resID);
		return result;
	}
	/**
	 * PRIVATE
	 */
	public static int getCharTextureID(char c) {
		int result=con.send(EngineID.MSG_GETCHARTEXTUREID,c);
		return result;
	}
	/**
	 * Load String texture, if you load String before game, use this method.
	 * @param resID
	 * @return BitmapID OpenGL use it, but you don't need
	 */
	public static int loadStringTexture(String str) {
		int result=con.send(EngineID.MSG_LOADSTRINGTEXTURE,str);
		return result;
	}
	
	
	public static Activity getActivity() {
		return (Activity)con.get(EngineID.GET_ACTIVTY);
	}
	public static Context getContext() {
		return getActivity();
	}
	/**
	 * @return Game screen width that you set in GameActivity
	 */
	public static int getGameScreenWidth() {
		return (Integer)con.get(EngineID.GET_GAMESCREENWIDTH);
	}
	/**
	 * @return Game screen height that you set in GameActivity
	 */
	public static int getGameScreenHeight() {
		return (Integer)con.get(EngineID.GET_GAMESCREENHEIGHT);
	}
}
