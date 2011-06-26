package org.orman.dbms.mysql;

import org.orman.dbms.DataTypeMapper;
import org.orman.dbms.Database;
import org.orman.dbms.DatabaseSchemaInspector;
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
	private DatabaseSchemaInspectorImpl schemaInspector;
	
	public MySQL(MySQLSettingsImpl settings){
		grammar = new MySQLGrammar();
		setSettings(settings);
		typeMapper = new DataTypeMapperImpl();
		executer = new QueryExecutionContainerImpl(getSettings());
		schemaInspector = new DatabaseSchemaInspectorImpl(getExecuter());
	}

	private void setSettings(MySQLSettingsImpl settings) {
		this.settings = settings;
	}

	public MySQLSettingsImpl getSettings() {
		return settings;
	}

	public DatabaseSchemaInspector getSchemaInspector() {
		return schemaInspector;
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
