package org.orman.mysql;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;

/**
 * MySQL implementation with JDBC (Connector/J)
 * 
 * @author alp
 */
public class MySQL implements Database{
	private MySQLSettingsImpl settings;
	private DataTypeMapperImpl typeMapper;
	private QueryExecutionContainer executer;
	
	public MySQL(MySQLSettingsImpl settings){
		setSettings(settings);
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(getSettings());
	}

	private void setSettings(MySQLSettingsImpl settings) {
		this.settings = settings;
	}

	public MySQLSettingsImpl getSettings() {
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
