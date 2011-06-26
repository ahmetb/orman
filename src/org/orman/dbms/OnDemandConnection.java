package org.orman.dbms;

import java.util.Random;

import org.orman.dbms.exception.IllegalConnectionOpenCallException;

/***
 * On demand connection requestor
 * 
 * @author oguz kartal
 *
 */

public class OnDemandConnection {
	private ConnectionEstablisher conn;
	private long securityCookie = 0;
	
	public OnDemandConnection(ConnectionEstablisher establisher) {
		Random rnd = new Random();
		securityCookie = establisher.hashCode() ^ rnd.nextInt(0x53C0013);
		conn = establisher;
		
		/*
		if (conn == null)
			throw new Exception("???");
		*/
	}
	
	public void checkCallCookie(long cookie) {
		if (cookie != securityCookie) {
			throw new IllegalConnectionOpenCallException("You can't directly call the open method");
		}
	}
	
	/***
	 * Requests connection on demand. 
	 * if database is already connected, it does nothing.
	 */
	public void requestConnection() {
		if (conn != null) {
			if (!conn.isAlive()) {
				try {
					conn.open(securityCookie);
				} catch (IllegalConnectionOpenCallException e) {}
			}
		}
	}
	
}
