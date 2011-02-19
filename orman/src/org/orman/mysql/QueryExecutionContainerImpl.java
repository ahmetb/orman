package org.orman.mysql;

import org.orman.datasource.QueryExecutionContainer;
import org.orman.sql.Query;

public class QueryExecutionContainerImpl implements QueryExecutionContainer {

	@Override
	public Object[][] executeForRowset(Query q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeForSingleValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeOnly(Query q) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getLastInsertId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getLastInsertId(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}
	
}