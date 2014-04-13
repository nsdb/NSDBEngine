package com.nsdb.engine.core.util;

/**
 * Communication means for this engine. GameController and GameObject use it, and many components will use it, if need to communicate to others
 * @author NSDB
 *
 */
public interface Communicable {
	
	/**
	 * Send message. parameter is more complicated than get(int), but returned value is simpler.
	 * @param type identifier of message
	 * @param content anything
	 * @return simple value you want
	 */
	public int send(int type,Object content);
	
	/**
	 * get value. parameter is simpler than send(int, Object), but can return various value.
	 * @param name identifier of message
	 * @return value you want
	 */
	public Object get(int name);

}
