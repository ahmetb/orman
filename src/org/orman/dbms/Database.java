package org.orman.dbms;

import org.orman.sql.SQLGrammarProvider;
import org.orman.dbms.PackageEntityInspector;

/**
 * Generic database interface that databases
 * should implement.
 *  
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * @author oguz kartal <0xffffffff@oguzkartal.net>
 * 
 */
public interface Database {
	public DataTypeMapper getTypeMapper();
	public SQLGrammarProvider getSQLGrammar();
	public QueryExecutionContainer getExecuter();
	public DatabaseSchemaInspector getSchemaInspector();
	public PackageEntityInspector getPackageInspector();
	public void closeConnection();
}
