package org.orman.dbms;

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
	 * checks whether table exists in existing database schema. comparison done
	 * case-insensitive.
	 * 
	 * @param tableName
	 *            table name to check.
	 * @return <code>true</code> if table exists in schema, <code>false</code>
	 *         if not.
	 */
	public boolean tableExists(String tableName);

	/**
	 * checks whether column of given table exists in existing database schema.
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

	/**
	 * checks existance of index on given table of existing database schema.
	 * 
	 * @param indexName
	 *            case-insensitive comparison is done.
	 * @param tableName
	 *            index defined on. might be ignored according to dbms.
	 * @return <code>true</code> if index with given name exists,
	 *         <code>false</code> if not.
	 */
	public boolean indexExists(String indexName, String tableName);
}
