
package org.orman.dbms.sqliteandroid;

import java.util.ArrayList;
import java.util.List;

import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.dbms.exception.QueryExecutionException;
import org.orman.sql.Query;
import org.orman.util.logging.Log;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class QueryExecutionContainerImpl implements QueryExecutionContainer {
	
	private SQLiteDatabase db;
	
	/**
	 * Initialize database, create db file if not exists.
	 * @param db
	 */
	public QueryExecutionContainerImpl(SQLiteDatabase db){
		this.db = db;
	}
	
	private void throwError(SQLiteException e){
		throw new QueryExecutionException("SQLiteAndroid error:" + e.toString());
	}
	

	@Override
	public void executeOnly(Query q) {		
		Log.trace("Executing: " + q);
		
		try {
			db.execSQL(q.getExecutableSql());
		} catch (SQLiteException e) {
			throwError(e);
		}
	}

	@Override
	public ResultList executeForResultList(Query q) {
		Log.trace("Executing: " + q);
		
		try {
			Cursor cur = db.rawQuery(q.getExecutableSql(), null);
			
			int columnCount = cur.getColumnCount();
			String[] colNames = cur.getColumnNames();
			
			List<Object[]> result = new ArrayList<Object[]>();
				
			@SuppressWarnings("unused")
			int rowIndex = 0;
			
			boolean hasRecord = cur.moveToFirst();
			
			while(hasRecord){
				Object[] row = new Object[columnCount];
				
				for(int j = 0; j < columnCount; j++){ // for each column
					row[j] = cur.getString(j); 
				}
				result.add(row);
				++rowIndex;
				
				hasRecord = cur.moveToNext();
			}
			
			if(result.size() > 0){
				Object[][] resultArr = new Object[result.size()][columnCount];
				int i = 0;
				for(Object[] row : result)
					resultArr[i++] = row;
				
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
		
		try {
			Cursor cur = db.rawQuery(q.getExecutableSql(), null);
			if (!cur.moveToNext()){
				return null;
			} else {
				return cur.getString(0);
			}
		} catch (SQLiteException e) {
			throwError(e);
		}
		return null;
	}

	@Override
	public Object getLastInsertId() {
		try {
			Cursor cur = db.rawQuery("SELECT last_insert_rowid()", null);
			
			boolean status = cur.moveToFirst();
			if (!status){
				return null;
			} else {
				return cur.getLong(0); // TODO returns long. test for int and String behavior.
			}
		} catch (SQLiteException e) {
			throwError(e);
			return null;
		}
	}

	@Override
	public <T> Object getLastInsertId(Class<T> ofType) {
		Object val = getLastInsertId();
		
		if (val == null) {
			Log.warn("last_insert_rowid() returned null from query. Propagating upwards as null, may cause anomalies.");
			return null;
		}
		
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
	public void close() {
		db.close();
	}

	@Override
	public boolean open(long cookie) {
		return true;
	}

	@Override
	public boolean isAlive() {
		if (db == null)
			return false;
		return db.isOpen();
	}

}