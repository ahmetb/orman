package org.orman.datasource;

import org.orman.datasource.exception.IllegalConnectionOpenCallException;

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
