package org.orman.dbms.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.orman.dbms.DatabaseSchemaInspector;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.sql.Query;
import org.orman.util.logging.Log;

public class DatabaseSchemaInspectorImpl implements DatabaseSchemaInspector {
	private final String COLS_SQL = "SELECT TABLE_NAME, COLUMN_NAME FROM "
			+ "INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=(DATABASE())";

	private QueryExecutionContainer db;
	Map<String, Set<String>> schema = null;

	public DatabaseSchemaInspectorImpl(QueryExecutionContainer mysql) {
		db = mysql;
	}

	@Override
	public void setQueryExecuter(QueryExecutionContainer db) {
		this.db = db;
	}

	private void extractSchema() {
		// postcondition: this.schema is not null or exception thrown.
		ResultList rs = this.db.executeForResultList(new Query(COLS_SQL));
		this.schema = new HashMap<String, Set<String>>();
		for (int i = 0; i < rs.getRowCount(); i++) {
			Object[] row = rs.getRow(i);
			String tbl = row[0].toString().toLowerCase(); // no need 4 nulchk
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
	}

	@Override
	public Map<String, Set<String>> getSchema() {
		if (this.schema == null)
			extractSchema();
		return this.schema;
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

}
