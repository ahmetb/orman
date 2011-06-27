package org.orman.dbms.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.orman.dbms.DatabaseSchemaInspector;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.sql.Query;

public class MySQLSchemaInspector implements DatabaseSchemaInspector {
	private final String INDEXES_SQL = "SELECT constraint_name as index_name," +
			" table_name FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE " +
			"TABLE_SCHEMA=DATABASE() AND constraint_name<>'PRIMARY'";
	private final String COLS_SQL = "SELECT TABLE_NAME, COLUMN_NAME FROM "
			+ "INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=(DATABASE())";

	private QueryExecutionContainer db;
	Map<String, Set<String>> schema = null; // <tablename, columns...>
	Map<String, String> indexes = null; // <indexname, tablename>

	public MySQLSchemaInspector(QueryExecutionContainer mysql) {
		db = mysql;
	}

	@Override
	public void setQueryExecuter(QueryExecutionContainer db) {
		this.db = db;
	}

	private void extractSchema() {
		// postcondition: this.schema is not null or exception thrown.
		
		// 1) Extract Tables & Columns
		ResultList rs = this.db.executeForResultList(new Query(COLS_SQL));
		this.schema = new HashMap<String, Set<String>>(rs.getRowCount());

		for (int i = 0; i < rs.getRowCount(); i++) {
			Object[] row = rs.getRow(i);
			String tbl = row[0].toString().toLowerCase(); // no need 4 nullchk
			String col = row[1].toString().toLowerCase();

			Set<String> cols = this.schema.get(tbl);
			if (cols == null) {
				cols = new TreeSet<String>();
				cols.add(col);
				this.schema.put(tbl, cols);
			} else {
				cols.add(col);
			}
		}
		
		// 2) Extract indexes
		rs = this.db.executeForResultList(new Query(INDEXES_SQL));
		this.indexes = new HashMap<String, String>(rs.getRowCount());

		for (int i = 0; i < rs.getRowCount(); i++) {
			Object[] row = rs.getRow(i);
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
		if (this.schema == null)
			extractSchema();
		if (tableName == null || columnName == null)
			throw new IllegalArgumentException();

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
