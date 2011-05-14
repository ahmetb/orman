package org.orman.sqlite.android;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.sql.SQLGrammarProvider;
import org.orman.sqlite.DataTypeMapperImpl;
import org.orman.sqlite.SQLiteGrammar;
import org.orman.util.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite wrapper for Android SDK 3 <code>database.sqlite
 * </code> package.
 * 
 * Shares same grammar and data type mapper with <code>org.orman.sqlite</code>
 * package.
 * 
 * @author alp
 */
public class SQLiteAndroid extends SQLiteOpenHelper implements Database {
	private static final int SQLITE_VERSION = 33; //TODO read from somewhere else ASAP
	private DataTypeMapper typeMapper;
	private QueryExecutionContainerImpl executer;
	private SQLiteGrammar grammar;
	
	private String databaseName;

	public SQLiteAndroid(Context context, String databaseName) {
		super(context, databaseName, null, SQLITE_VERSION);

		this.databaseName = databaseName;
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(null); //bind database onCreate.
		grammar = new SQLiteGrammar();
	}

	@Override
	public QueryExecutionContainer getExecuter() {
		return executer;
	}

	@Override
	public DataTypeMapper getTypeMapper() {
		return typeMapper;
	}

	@Override
	public void closeConnection() {
		executer.close();
		Log.info("Connection terminated successfully.");
	}

	@Override
	public SQLGrammarProvider getSQLGrammar() {
		return grammar;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.info("SQLite database onCreate invoked. Database path %s.", db.getPath());
		//TODO currently defers creation of database to SchemeCreationPolicy.
		// and does not create database here.
		
		executer.setDatabase(db); // no need for readable db.
		// drop scheme first
		// construct scheme
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO not implemented. have a destroySchemeQueries function in MappingSession.
		Log.info(
				"SQLite database onUpdate invoked. Database path %s. Old %d, new %d.",
				db.getPath(), oldVersion, newVersion);
		executer.setDatabase(this.getWritableDatabase());
		
		// drop scheme 
		// construct scheme
	}

	public String getDatabaseName() {
		return databaseName;
	}
}
