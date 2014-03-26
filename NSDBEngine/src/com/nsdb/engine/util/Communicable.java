package com.nsdb.engine.util;

public interface Communicable {
	
	public int send(int type,Object content);
	public Object get(int name);

}
