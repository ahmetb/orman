package org.orman.sqlite;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;

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
 * @author alp
 */
public class SQLite implements Database {
	private SQLiteSettingsImpl settings;
	private DataTypeMapper typeMapper;
	private QueryExecutionContainerImpl executer;
	
	public SQLite(String file){
		setSettings(new SQLiteSettingsImpl(file));
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(getSettings());
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
		System.out.println("Connection terminated successfully."); //TODO log.
	}
	
}
