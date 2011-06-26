package org.orman.dbms;

import org.orman.dbms.exception.IllegalConnectionOpenCallException;

/***
 * Connection establish provider interface for {@link QueryExecutionContainer}
 * @author oguz kartal
 *
 */

public interface ConnectionEstablisher {
	public boolean open(long cookie) throws IllegalConnectionOpenCallException;
	
	public void close();
	
	public boolean isAlive();
}
