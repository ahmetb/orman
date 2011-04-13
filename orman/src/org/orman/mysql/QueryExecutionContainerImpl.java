
package org.orman.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.gjt.mm.mysql.Driver;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.datasource.ResultList;
import org.orman.datasource.exception.QueryExecutionException;
import org.orman.sql.Query;

public class QueryExecutionContainerImpl implements QueryExecutionContainer {
	
	private MySQLSettingsImpl settings;
	private Connection conn = null;
	
	/**
	 * Initialize database, create db file if not exists.
	 * @param settings
	 */
	public QueryExecutionContainerImpl(MySQLSettingsImpl settings){
		this.settings = settings;
		
		Properties props = new Properties();
		props.put("user", this.settings.getUsername());
		props.put("password", this.settings.getPassword());
		
		// Establish DB connection.
		try {
			DriverManager.registerDriver(new Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + this.settings.getHost() + ":"
							+ this.settings.getPort() + "/" + this.settings.getDatabase(), props);
			conn.setAutoCommit(this.settings.isAutoCommit());
			
		} catch (SQLException e) {
			throwError(e);
		}
		if (conn == null)
			throwError(new SQLException("Could not establish connection to database."));
	}
	
	private void throwError(SQLException e){
		throw new QueryExecutionException("MySQL error:" + e.toString());
	}

	@Override
	public ResultList executeForResultList(Query q) {
		System.out.println("Executing: " + q); // TODO log.
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(q.getExecutableSql());
			
			ResultSet rs = stmt.getResultSet();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			List<Object[]> result = new ArrayList<Object[]>();
			
			while(rs.next()){ // for each row.
				Object[] row = new Object[columnCount]; 
				
				for (int colIndex = 0; colIndex < columnCount; colIndex++) {
					row[colIndex] = rs.getObject(colIndex+1); // starts from 1.
				}
				result.add(row);
			}
			
			if(result.size() > 0){
				Object[][] resultArr = new Object[result.size()][];
				// convert to array.
				int i = 0;
				for(Object[] row : result)
					resultArr[i++] = row;
				
				// get column names of result
				String[] columnNames = new String[columnCount];
				for (int j = 0; j < columnNames.length; j++) {
					columnNames[j] = rsMeta.getColumnName(j+1); // starts from 1.
				}
				
				return new ResultList(columnNames, resultArr);
			}
		} catch (SQLException ex) {
			throwError(ex);
		}	
		return null;
	}

	@Override
	public Object executeForSingleValue(Query q) {
		System.out.println("Executing: " + q); // TODO log.
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(q.getExecutableSql());
			ResultSet rs = stmt.getResultSet();
			
			while(rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
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
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(q.getExecutableSql());
		} catch (SQLException e) {
			throwError(e);
		}
	}

	@Override
	public Object getLastInsertId() {
		String lastIdQuery = "SELECT LAST_INSERT_ID();";
		
		System.out.println("Executing: " + lastIdQuery); // TODO log.
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(lastIdQuery);
			rs.next();
			return rs.getObject(1);
		} catch (SQLException e) {
			throwError(e);
		}
		
		return null;
	}

	@Override
	public <T> Object getLastInsertId(Class<T> ofType) {
		Object val = getLastInsertId();
		
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
		try {
			conn.close();
		} catch (SQLException e) {
			throwError(e);
		}
	}
}
