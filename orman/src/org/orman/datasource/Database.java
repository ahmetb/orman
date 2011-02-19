package org.orman.datasource;

/**
 * Generic database interface that databases
 * should implement.
 *  
 * @author alp
 */
public interface Database {
	public DataTypeMapper getTypeMapper();
	public QueryExecutionContainer getExecuter();
}
