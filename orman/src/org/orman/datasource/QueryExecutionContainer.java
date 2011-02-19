package org.orman.datasource;

import java.util.List;

import org.orman.mapper.Entity;
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
	
	public <E> List<E> executeForRowset(Query q, Entity e);
	
	public Object executeForSingleValue(Query q);
	
	public Object getLastInsertId();
	
	public <T> Object getLastInsertId(Class<T> ofType);
	
	public void close();
}
