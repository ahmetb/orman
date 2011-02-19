
package org.orman.sqlite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.orman.datasource.QueryExecutionContainer;
import org.orman.datasource.exception.QueryExecutionException;
import org.orman.mapper.Entity;
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
		
		dbFile = new File(this.settings.getFilePath());
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
		System.out.println("Executing: " + q); // TODO log.
		return null;
	}
	
	@Override
	public <E> List<E> executeForRowset(Query q, Entity e) {
		System.out.println("Executing: " + q); // TODO log.
		List<E> resultList = new ArrayList<E>();
		
		try {
			SQLiteStatement s = db.prepare(q.getExecutableSql());
			while(s.step()){
				// TODO continue from here.
			}
		} catch (SQLiteException ex) {
			throwError(ex);
		}	
		return resultList;
	}

	@Override
	public Object executeForSingleValue(Query q) {
		System.out.println("Executing: " + q); // TODO log.
		
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
			return new Long(val.toString());
		}   
		return val;
	}
	

	@Override
	public void close() {
		db.dispose();
	}

}