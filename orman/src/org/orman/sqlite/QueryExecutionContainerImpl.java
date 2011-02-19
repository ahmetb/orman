
package org.orman.sqlite;

import java.io.File;

import org.orman.datasource.QueryExecutionContainer;
import org.orman.sql.Query;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;

// TODO close()
public class QueryExecutionContainerImpl implements QueryExecutionContainer {
	
	private SQLiteSettingsImpl settings;
	private File dbFile;
	private SQLiteConnection db;
	
	/**
	 * Initialize database, create db file if not exists.
	 * @param settings
	 */
	public QueryExecutionContainerImpl(SQLiteSettingsImpl settings){
		this.settings = settings;
		
		dbFile = new File(settings.getFilePath());
		db = new SQLiteConnection(dbFile);
		try {
			db.open(true);
		} catch (SQLiteException e) {
			throwError(e);
		}
		
		// TODO create db if not exist.
	}
	
	private void throwError(SQLiteException e){
		throw new RuntimeException("SQLite error:" + e.toString());
	}

	@Override
	public Object[][] executeForRowset(Query q) {
		return null;
	}

	@Override
	public Object executeForSingleValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeOnly(Query q) {
		
	}

	@Override
	public Object getLastInsertId() {
		try {
			return db.getLastInsertId(); // TODO returns long. test for int and String behavior. 
		} catch (SQLiteException e) {
			throwError(e);
			return null;
		}
	}
}