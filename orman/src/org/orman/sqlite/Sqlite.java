package org.orman.sqlite;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;

/**
 * SQLite implementation with SqlJet library.
 * 
 * @author alp
 */
public class Sqlite implements Database {
	private SQLiteSettingsImpl settings;
	private DataTypeMapper typeMapper;
	private QueryExecutionContainerImpl executer;
	
	public Sqlite(String file){
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
}
