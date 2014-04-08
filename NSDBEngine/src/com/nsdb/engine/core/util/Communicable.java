package com.nsdb.engine.core.util;

public interface Communicable {
	
	public int send(int type,Object content);
	public Object get(int name);

}
