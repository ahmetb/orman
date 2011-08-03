
package org.orman.dbms.sqlite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.orman.dbms.OnDemandConnection;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.dbms.exception.IllegalConnectionOpenCallException;
import org.orman.dbms.exception.QueryExecutionException;
import org.orman.sql.Query;
import org.orman.util.logging.Log;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

// TODO dispose() time.
public class QueryExecutionContainerImpl implements QueryExecutionContainer {
	
	private SQLiteSettingsImpl settings;
	private File dbFile;
	private SQLiteConnection db;
	private OnDemandConnection demandConnector = 
			new OnDemandConnection(this);
	
	/**
	 * Initialize database
	 * @param settings
	 */
	public QueryExecutionContainerImpl(SQLiteSettingsImpl settings){
		this.settings = settings;
	}
	
	private void throwError(SQLiteException e){
		throw new QueryExecutionException("SQLite error:" + e.toString());
	}

	@Override
	public ResultList executeForResultList(Query q) {
		Log.trace("Executing: " + q); 
		
		demandConnector.requestConnection();
		
		try {
			SQLiteStatement s = db.prepare(q.getExecutableSql());
			
			int columnCount = s.columnCount();
			String[] colNames = new String[columnCount];
			List<Object[]> result = new ArrayList<Object[]>();
				
			int rowIndex = 0;
				
			while(s.step()){ // for each row.
				Object[] row = new Object[columnCount];
				
				for(int j = 0; j < columnCount; j++){ // for each column
					row[j] = s.columnValue(j); 
				}
				result.add(row);
				++rowIndex;
			}
			
			if(result.size() > 0){
				Object[][] resultArr = new Object[result.size()][columnCount];
				int i = 0;
				for(Object[] row : result)
					resultArr[i++] = row;
				
				for(int j = 0; j < columnCount; j++){
					colNames[j] = s.getColumnName(j);
				}
				
				return new ResultList(colNames, resultArr);
			}
		} catch (SQLiteException ex) {
			throwError(ex);
		}	
		return null;
	}

	@Override
	public Object executeForSingleValue(Query q) {
		Log.trace("Executing: " + q); 
		
		demandConnector.requestConnection();
		
		try {
			SQLiteStatement s = db.prepare(q.getExecutableSql());
			while(s.step()){
				return s.columnValue(0);
			}
		} catch (SQLiteException e) {
			throwError(e);
		}
		return null;
	}

	/**
	 * Only executes the query without obtaining any results.
	 * throws {@link QueryExecutionException} if error occurs.
	 */
	@Override
	public void executeOnly(Query q) {
		Log.trace("Executing: " + q); // TODO log.
		
		demandConnector.requestConnection();
		
		try {
			db.exec(q.getExecutableSql());
		} catch (SQLiteException e) {
			throwError(e);
		}
	}

	@Override
	public Object getLastInsertId() {
		
		demandConnector.requestConnection();
		
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
			return new Integer(val.toString()); // TODO inefficient?
		} else if(ofType.equals(Long.class) || ofType.equals(Long.TYPE)){
			return new Long(val.toString()); // TODO inefficient? 
		}   
		return val;
	}
	
	@Override
	public boolean isAlive() {
		if (db == null)
			return false;
		
		return db.isOpen();
	}
	
	@Override
	public boolean open(long cookie) throws IllegalConnectionOpenCallException {
		
		demandConnector.checkCallCookie(cookie);
		
		dbFile = new File(this.settings.getFilePath());
		db = new SQLiteConnection(dbFile);
		
		try {
			db.open(true);
			return true;
		} catch (SQLiteException e) {
			throwError(e);
		}
		
		return false;
	}

	@Override
	public void close() {
		if (db == null)
			return; // if not initialized yet
		
		db.dispose();
		db = null;
	}
}