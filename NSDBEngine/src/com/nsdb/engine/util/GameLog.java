package com.nsdb.engine.util;

import java.util.Calendar;

/**
 * Logger for this game.
 *
 */
public class GameLog {
	
	private static Calendar cal;
	
	public static final boolean DEBUG=true;
	
	public static void init(String type) {
		if(cal == null) cal=Calendar.getInstance();
		info("Logger inited : type-"+type);
	}
	
	public static void debug(String msg) {
		if(DEBUG) log("debug",msg);
	}

	public static void info(String msg) {
		log("info",msg);
	}
	
	public static void danger(String msg) {
		log("danger",msg);
	}
	
	public static void error(String msg) {
		log("error",msg);
	}
	
	public static void fetal(String msg) {
		log("fetal",msg);
	}

	private static void log(String level,String msg) {
		String result="["+level+"]["+getCurrentTimeString()+"] "+msg;
		System.out.println(result);
	}

	private static String getCurrentTimeString() {
		cal.setTimeInMillis(System.currentTimeMillis());
		return ""+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+
				cal.get(Calendar.SECOND)+":"+cal.get(Calendar.MILLISECOND);
	}
	
}
