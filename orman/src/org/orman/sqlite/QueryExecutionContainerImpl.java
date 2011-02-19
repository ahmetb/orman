
package org.orman.sqlite;

import java.io.File;

import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.datasource.exception.QueryExecutionException;
import org.orman.sql.Query;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

// TODO dispose() time.
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
	}
	
	private void throwError(SQLiteException e){
		throw new QueryExecutionException("SQLite error:" + e.toString());
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

	/**
	 * Only executes the query without obtaining any results.
	 * throws {@link QueryExecutionException} if error occurs.
	 */
	@Override
	public void executeOnly(Query q) {
		System.out.println("Executing: " + q); // TODO log.
		try {
			db.exec(q.getExecutableSql());
		} catch (SQLiteException e) {
			throwError(e);
		}
	}

	@Override
	public Object getLastInsertId() {
		try {
			return new Long(db.getLastInsertId()); // TODO returns long. test for int and String behavior. 
		} catch (SQLiteException e) {
			throwError(e);
			return null;
		}
	}

	@Override
	public <T> Object getLastInsertId(Class<T> ofType) {
		Object val = getLastInsertId();
		
		if(ofType.equals(String.class)){
			return new String(val.toString());
		} else if(ofType.equals(Integer.class) || ofType.equals(Integer.TYPE)){
			return new Integer(val.toString());
		} else if(ofType.equals(Long.class) || ofType.equals(Long.TYPE)){
			return new Integer(val.toString());
		}   
		return val;
	}
}