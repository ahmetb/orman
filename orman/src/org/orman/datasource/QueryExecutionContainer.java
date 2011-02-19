package org.orman.datasource;

import org.orman.sql.Query;

/**
 * Provides a unified query execution container such
 * that every DBMS can return requested type 
 * of {@link Query} results. Wraps the database
 * implementation.
 * 
 * @author alp
 *
 */
public interface QueryExecutionContainer {
	public void executeOnly(Query q);
	
	public Object[][] executeForRowset(Query q);
	
	public Object executeForSingleValue();
	
	public Object getLastInsertId();
	
	public <T> Object getLastInsertId(Class<T> ofType);
}
