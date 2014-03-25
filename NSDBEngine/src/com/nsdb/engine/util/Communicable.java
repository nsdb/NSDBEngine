package com.nsdb.engine.util;

public interface Communicable {
	
	public int send(String type,Object content);
	public Object get(String name);

}
