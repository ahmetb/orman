package org.orman.dbms.mysql;

import org.orman.dbms.DataTypeMapper;
import org.orman.dbms.Database;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.sql.SQLGrammarProvider;

/**
 * MySQL implementation with JDBC (Connector/J).
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
public class MySQL implements Database {
	private MySQLSettingsImpl settings;
	private DataTypeMapperImpl typeMapper;
	private QueryExecutionContainer executer;
	private MySQLGrammar grammar;
	
	public MySQL(MySQLSettingsImpl settings){
		setSettings(settings);
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(getSettings());
		grammar = new MySQLGrammar();
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

	@Override
	public void closeConnection() {
		executer.close();
	}

	@Override
	public SQLGrammarProvider getSQLGrammar() {
		return grammar;
	}
}
