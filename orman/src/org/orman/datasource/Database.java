package org.orman.datasource;

public interface Database {
	public DataTypeMapper getTypeMapper();
	public QueryExecutionContainer getExecuter();
}
