package com.nsdb.engine.core.controller;

import android.app.Activity;
import android.content.Context;

import com.nsdb.engine.core.constant.EngineID;
import com.nsdb.engine.core.util.Communicable;
import com.nsdb.engine.core.util.GameLog;

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
	public static int loadChunk(int resID) {
		int result=con.send(EngineID.MSG_LOADCHUNK, resID);
		return result;
	}
	public static boolean unloadChunk(int chunkID) {
		int result=con.send(EngineID.MSG_UNLOADCHUNK, chunkID);
		return result==1;
	}
	public static boolean playChunk(int chunkID) {
		int result=con.send(EngineID.MSG_PLAYCHUNK, chunkID);
		return result==1;
	}
	
	public static int getBitmapTextureID(int resID) {
		int result=con.send(EngineID.MSG_GETBITMAPTEXTUREID,resID);
		return result;
	}
	public static int loadBitmapTexture(int resID) {
		int result=con.send(EngineID.MSG_LOADBITMAPTEXTURE,resID);
		return result;
	}
	public static int getCharTextureID(char c) {
		int result=con.send(EngineID.MSG_GETCHARTEXTUREID,c);
		return result;
	}
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
	public static int getGameScreenWidth() {
		return (Integer)con.get(EngineID.GET_GAMESCREENWIDTH);
	}
	public static int getGameScreenHeight() {
		return (Integer)con.get(EngineID.GET_GAMESCREENHEIGHT);
	}
}
