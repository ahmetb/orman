package org.orman.datasource;

import org.orman.sql.Query;

public interface QueryExecutionContainer {
	public void executeOnly(Query q);
	
	public Object[][] executeForRowset(Query q);
	
	public Object executeForSingleValue();
	
	public Object getLastInsertId();
}
