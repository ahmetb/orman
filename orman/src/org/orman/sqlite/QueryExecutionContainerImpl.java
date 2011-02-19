
package org.orman.sqlite;

import java.io.File;

import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.sql.Query;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

// TODO close()
public class QueryExecutionContainerImpl implements QueryExecutionContainer {
	
	private SQLiteSettingsImpl settings;
	private File dbFile;
	private SqlJetDb db;
	
	/**
	 * Initialize database, create db file if not exists.
	 * @param settings
	 */
	public QueryExecutionContainerImpl(SQLiteSettingsImpl settings){
		this.settings = settings;
		
		dbFile = new File(settings.getFilePath());
		try {
			db = SqlJetDb.open(dbFile, true);
			db.beginTransaction(SqlJetTransactionMode.WRITE);
			
			if (dbFile.length() < 5){ // some magic number, create database.
				dbFile.delete();
				db.getOptions().setUserVersion(1);
				System.out.println("SQLite Database created."); // TODO log.
			}
			db.commit();
		} catch (SqlJetException e) {
			throwError(e);
		}
	}
	
	private void throwError(SqlJetException e){
		throw new RuntimeException("SQLite error:" + e.getMessage());
	}

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
		String sql = "CREATE TABLE employees (second_name TEXT NOT NULL PRIMARY KEY , first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)";
		try {
			db.beginTransaction(SqlJetTransactionMode.WRITE);
			db.createTable(sql);
			db.commit();
		} catch (SqlJetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getLastInsertId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		Database db = new Sqlite("lite.db");
		db.getExecuter().executeOnly(null);
	}
	
}