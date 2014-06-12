package com.nsdb.engine.core.util;

import java.util.Calendar;

import android.util.Log;

/**
 * Logger for this game.
 *
 */
public class GameLog {
	
	private static Calendar cal;
	
	public static void init(String type) {
		if(cal == null) cal=Calendar.getInstance();
		info("GameLog","Logger inited : type-"+type);
	}
	
	public static void debug(Object obj, String msg) {
		if(obj.getClass()==String.class)
			Log.d((String)obj,msg);
		else
			Log.d(obj.getClass().getSimpleName(),msg);
	}
	
	public static void info(Object obj, String msg) {
		if(obj.getClass()==String.class)
			Log.i((String)obj,msg);
		else
			Log.i(obj.getClass().getSimpleName(),msg);
	}
	
	public static void danger(Object obj, String msg) {
		if(obj.getClass()==String.class)
			Log.w((String)obj,msg);
		else
			Log.w(obj.getClass().getSimpleName(),msg);
	}
	
	public static void error(Object obj, String msg) {
		if(obj.getClass()==String.class)
			Log.e((String)obj,msg);
		else
			Log.e(obj.getClass().getSimpleName(),msg);
	}
	
	public static void fetal(Object obj, String msg) {
		if(obj.getClass()==String.class)
			Log.wtf((String)obj,msg);
		else
			Log.wtf(obj.getClass().getSimpleName(),msg);
	}

}
