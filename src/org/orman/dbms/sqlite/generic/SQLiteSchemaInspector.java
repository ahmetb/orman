package org.orman.dbms.sqlite.generic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.orman.dbms.DatabaseSchemaInspector;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.sql.Query;

public class SQLiteSchemaInspector implements DatabaseSchemaInspector {
	private final String TABLES_SQL = "SELECT name, sql FROM sqlite_master" +
			" WHERE type='table' AND name NOT IN ('sqlite_master', 'sqlite_sequence')";
	private final String INDEXES_SQL = "SELECT name, tbl_name FROM sqlite_master" +
			" WHERE type='index'";

	private QueryExecutionContainer db;
	Map<String, Set<String>> schema = null; // <table, fields...>
	Map<String, String> indexes = null; // <name, table>

	public SQLiteSchemaInspector(QueryExecutionContainer sqlite) {
		db = sqlite;
	}

	@Override
	public void setQueryExecuter(QueryExecutionContainer db) {
		this.db = db;
	}

	private void extractSchema() {
		// postcondition: this.schema is not null or exception thrown.
		
		// 1) Get Tables & Column Metadata
		ResultList rs = this.db.executeForResultList(new Query(TABLES_SQL));
		this.schema = new HashMap<String, Set<String>>(rs.getRowCount());
		
		// extract column names from DDL CREATE TABLE query.
		for (int i = 0; i < rs.getRowCount(); i++) {
			Object[] row = rs.getRow(i); // 0: tbl name, 1: DDL query, not-null
			String tableName = row[0].toString().toLowerCase();
			String ddl = row[1].toString().replace("\n", "").replace("\"", "")
					.replace("'", "").replace("\t",""); // no need for nullchk.
			ddl = ddl.substring(ddl.indexOf('(') + 1, ddl.indexOf(')'));
			String[] columnDefs = ddl.split(",");
			Set<String> colNames = new HashSet<String>(columnDefs.length);
			
			for(String cD : columnDefs){
				String colName = new StringTokenizer(cD).nextToken().toLowerCase();
				
				if (!colName.equals("foreign")){ // discard foreign key clause
					colNames.add(colName);
				}
			}
			this.schema.put(tableName, colNames);
		}
		
		// 2) Get Indexes.
		rs = this.db.executeForResultList(new Query(INDEXES_SQL));
		this.indexes = new HashMap<String, String>(rs.getRowCount());
		for (int i = 0; i < rs.getRowCount(); i++) {
			Object[] row = rs.getRow(i); // 0: tbl name, 1: DDL query, not-null
			String indexName = row[0].toString().toLowerCase();
			String tableName = row[1].toString().toLowerCase();
			
			this.indexes.put(indexName, tableName);
		}
	}

	@Override
	public boolean tableExists(String tableName) {
		if (this.schema == null)
			extractSchema();
		if (tableName == null)
			throw new IllegalArgumentException();

		return this.schema.containsKey(tableName.toLowerCase());
	}

	@Override
	public boolean columnExists(String tableName, String columnName) {
		if (tableName == null || columnName == null)
			throw new IllegalArgumentException();
		if (this.schema == null)
			extractSchema();

		Set<String> cols = this.schema.get(tableName.toLowerCase());

		if (cols == null)
			return false;
		else {
			return cols.contains(columnName.toLowerCase());
		}
	}

	@Override
	public boolean indexExists(String indexName, String tableName) {
		if (indexName == null)
			throw new IllegalArgumentException();
		if (this.indexes == null)
			extractSchema();
		
		String tblOfIndex = this.indexes.get(indexName.toLowerCase());
		if (tblOfIndex == null) return false;
		else return tblOfIndex.equalsIgnoreCase(tableName);
	}

}
