package org.orman.dbms;

import java.util.Map;
import java.util.Set;

/**
 * An interface that provides methods to concrete classes which are going to
 * investigate enclosing database schema of DBMS implementation.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
public interface DatabaseSchemaInspector {
	/**
	 * sets query executer in implementation
	 * 
	 * @param db
	 *            query execution container to execute inspection queries.
	 */
	public void setQueryExecuter(QueryExecutionContainer db);

	/**
	 * @return map of tables each associated with a set of their columns. all
	 *         names are in lowercase for comparison.
	 */
	Map<String, Set<String>> getSchema();

	/**
	 * comparison done case-insensitive.
	 * 
	 * @param tableName
	 *            table name to check.
	 * @return <code>true</code> if table exists in schema, <code>false</code>
	 *         if not.
	 */
	public boolean tableExists(String tableName);

	/**
	 * comparison done case-insensitive.
	 * 
	 * @param tableName
	 *            table name to check.
	 * @param columnName
	 *            column name to check in <code>tableName</code>
	 * @return <code>true</code> if column exists in table of schema,
	 *         <code>false</code> if not.
	 */
	public boolean columnExists(String tableName, String columnName);

	// TODO add indexExists(String tableName, String indexName)?
}
