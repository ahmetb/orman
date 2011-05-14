package org.orman.sqliteandroid;

import java.util.EnumMap;
import java.util.Map;

import org.orman.sql.QueryType;
import org.orman.sql.SQLGrammarProvider;
import org.orman.sql.exception.QueryTypeNotImplementedException;

/**
 * MySQL grammar for SQL language. Update accordingly {@link SQLGrammarProvider}
 *
 * Also update {@link org.orman.sqlite.SQLiteGrammar}.
 *
 * @see SQLGrammarProvider
 * 
 * @author Ahmet Alp Balkan <ahmetalpbalkan at gmail.com>
 *
 */
public class SQLiteGrammar implements SQLGrammarProvider {
	@SuppressWarnings("serial")
	private static Map<QueryType, String> grammar = new EnumMap<QueryType, String>(QueryType.class){{
		// put statements into map;
		put(QueryType.USE_DATABASE, "USE DATABASE {DATABASE}"); 
		
		put(QueryType.CREATE_TABLE, "CREATE TABLE {TABLE_LIST} ({COLUMN_OR_CONSTRAINT_DESCRIPTION_LIST})");
		put(QueryType.CREATE_TABLE_IF_NOT_EXSISTS, "CREATE TABLE IF NOT EXISTS {TABLE_LIST} ({COLUMN_OR_CONSTRAINT_DESCRIPTION_LIST})");
		put(QueryType.DROP_TABLE, "DROP TABLE {TABLE_LIST}");
		put(QueryType.DROP_TABLE_IF_EXISTS, "DROP TABLE IF EXISTS {TABLE_LIST}");
		
		put(QueryType.SELECT, "SELECT {SELECT_COLUMN_LIST} FROM {TABLE_LIST} {JOIN}{WHERE}{GROUP_BY}{HAVING}{ORDER_BY}{LIMIT}");
		put(QueryType.SELECT_DISTINCT, "SELECT DISTINCT {SELECT_COLUMN_LIST} FROM {TABLE_LIST} {JOIN}{WHERE}{GROUP_BY}{HAVING}{ORDER_BY}{LIMIT}");
		
		put(QueryType.INSERT, "INSERT INTO {TABLE_LIST} ({COLUMN_LIST}) VALUES ({VALUE_LIST})");
		put(QueryType.UPDATE, "UPDATE {TABLE_LIST} SET {COLUMN_VALUE_LIST} {WHERE}");
		put(QueryType.DELETE, "DELETE FROM {TABLE_LIST} {WHERE}");
		
		put(QueryType.CREATE_INDEX, "CREATE INDEX {INDEX_NAME} ON {TABLE_LIST} ({SELECT_COLUMN_LIST})"); 
		put(QueryType.CREATE_UNIQUE_INDEX, "CREATE UNIQUE INDEX {INDEX_NAME} ON {TABLE_LIST} ({SELECT_COLUMN_LIST})"); 
		put(QueryType.CREATE_INDEX_IF_NOT_EXISTS, "CREATE INDEX IF NOT EXISTS {INDEX_NAME} ON {TABLE_LIST} ({SELECT_COLUMN_LIST})"); 
		put(QueryType.CREATE_UNIQUE_INDEX_IF_NOT_EXISTS, "CREATE UNIQUE INDEX IF NOT EXISTS {INDEX_NAME} ON {TABLE_LIST} ({SELECT_COLUMN_LIST})"); 
		put(QueryType.DROP_INDEX, "DROP INDEX {INDEX_NAME}"); 
		put(QueryType.DROP_INDEX_IF_EXISTS, "DROP INDEX IF EXISTS {INDEX_NAME} "); 

		put(QueryType.BEGIN_TRANSACTION, "BEGIN TRANSACTION"); 
		put(QueryType.COMMIT_TRANSACTION, "COMMIT"); 
		put(QueryType.ROLLBACK_TRANSACTION, "ROLLBACK"); 
	}};
	
	
	@Override
	public String getTemplate(QueryType type) {
		String tpl = grammar.get(type);
		if (tpl == null)
			throw new QueryTypeNotImplementedException(type.toString());
		return tpl;
	}
}
