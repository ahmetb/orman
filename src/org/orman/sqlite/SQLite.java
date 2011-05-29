package org.orman.sqlite;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.sql.SQLGrammarProvider;
import org.orman.sqlite.generic.DataTypeMapperImpl;
import org.orman.sqlite.generic.SQLiteGrammar;
import org.orman.util.logging.Log;

/**
 * SQLite implementation with sqlite4java library
 * {@link http://code.google.com/p/sqlite4java/}
 * 
 * Known SQLite problems:
 * - RIGHT and FULL OUTER JOIN unsupported.
 * - Writing to VIEWs unsupported.
 * - see http://www.sqlite.org/omitted.html for more.
 * 
 * Known framework problems on SQLite
 * - Full-text search extension fts3 not supported.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
public class SQLite implements Database {
	private SQLiteSettingsImpl settings;
	private DataTypeMapper typeMapper;
	private QueryExecutionContainerImpl executer;
	private SQLiteGrammar grammar;
	
	public SQLite(String file){
		setSettings(new SQLiteSettingsImpl(file));
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(getSettings());
		grammar = new SQLiteGrammar();
	}

	private void setSettings(SQLiteSettingsImpl settings) {
		this.settings = settings;
	}

	public SQLiteSettingsImpl getSettings() {
		return settings;
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
	
}
