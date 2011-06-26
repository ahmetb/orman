package org.orman.dbms.mysql;

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
import org.orman.dbms.OnDemandConnection;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.dbms.ResultList;
import org.orman.dbms.exception.DatasourceConnectionException;
import org.orman.dbms.exception.IllegalConnectionOpenCallException;
import org.orman.dbms.exception.QueryExecutionException;
import org.orman.exception.OrmanException;
import org.orman.sql.Query;
import org.orman.util.logging.Log;

/**
 * MySQL query executer implemented using Connector/J JDBC plugin.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
public class QueryExecutionContainerImpl implements QueryExecutionContainer {

	private MySQLSettingsImpl settings;
	private Connection conn = null;
	private OnDemandConnection demandConnector = 
			new OnDemandConnection(this);

	/**
	 * Initialize database
	 * 
	 * @param settings
	 */
	public QueryExecutionContainerImpl(MySQLSettingsImpl settings) {
		this.settings = settings;
	}

	private void throwError(OrmanException e) {
		throw new OrmanException("MySQL error:" + e.getMessage());
	}

	private void throwError(SQLException e) {
		throw new QueryExecutionException("MySQL error:" + e.getMessage());
	}

	@Override
	public ResultList executeForResultList(Query q) {
		Log.trace(q.getExecutableSql());
		
		demandConnector.requestConnection();

		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(q.getExecutableSql());

			ResultSet rs = stmt.getResultSet();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			List<Object[]> result = new ArrayList<Object[]>();

			while (rs.next()) { // for each row.
				Object[] row = new Object[columnCount];

				for (int colIndex = 0; colIndex < columnCount; colIndex++) {
					row[colIndex] = rs.getObject(colIndex + 1); // starts from
																// 1.
				}
				result.add(row);
			}

			if (result.size() > 0) {
				Object[][] resultArr = new Object[result.size()][];
				// convert to array.
				int i = 0;
				for (Object[] row : result)
					resultArr[i++] = row;

				// get column names of result
				String[] columnNames = new String[columnCount];
				for (int j = 0; j < columnNames.length; j++) {
					columnNames[j] = rsMeta.getColumnName(j + 1); // starts from
																	// 1.
				}

				Log.trace("  Query returned %d rows.", result.size());
				return new ResultList(columnNames, resultArr);
			}
		} catch (SQLException ex) {
			throwError(ex);
		}
		return null;
	}

	@Override
	public Object executeForSingleValue(Query q) {
		Log.trace(q.getExecutableSql());

		demandConnector.requestConnection();
		
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(q.getExecutableSql());
			ResultSet rs = stmt.getResultSet();

			while (rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			throwError(e);
		}
		return null;
	}

	/**
	 * Only executes the query without obtaining any results. throws
	 * {@link QueryExecutionException} if error occurs.
	 */
	@Override
	public void executeOnly(Query q) {
		Log.trace(q.getExecutableSql());

		demandConnector.requestConnection();
		
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(q.getExecutableSql());
		} catch (SQLException e) {
			throwError(e);
		}
	}

	@Override
	public Object getLastInsertId() {
		String lastIdQuery = "SELECT LAST_INSERT_ID();";

		demandConnector.requestConnection();
		
		Log.trace(lastIdQuery);

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

		if (ofType.equals(String.class)) {
			return new String(val.toString());
		} else if (ofType.equals(Integer.class) || ofType.equals(Integer.TYPE)) {
			return new Integer(val.toString()); // TODO inefficient?
		} else if (ofType.equals(Long.class) || ofType.equals(Long.TYPE)) {
			return new Long(val.toString()); // TODO inefficient?
		}
		return val;
	}

	@Override
	public boolean isAlive() {
		if (conn == null)
			return false;
		
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			Log.error(e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public boolean open(long cookie) throws IllegalConnectionOpenCallException {
		
		demandConnector.checkCallCookie(cookie);
		
		Properties props = new Properties();
		props.put("user", this.settings.getUsername());
		props.put("password", this.settings.getPassword());

		// Establish DB connection.
		try {
			Log.info("Trying to establish MySQL connection to %s at %s.",
					this.settings.getHost(), this.settings.getPort());

			DriverManager.registerDriver(new Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + this.settings.getHost() + ":"
							+ this.settings.getPort() + "/"
							+ this.settings.getDatabase(), props);

			conn.setAutoCommit(this.settings.isAutoCommit());

		} catch (SQLException e) {
			throwError(new DatasourceConnectionException(e.getMessage()));
		}

		if (conn == null){
			throwError(new DatasourceConnectionException(
					"Could not establish connection to database."));
			return false;
		} else 
			Log.info("Connection established to the database.");
		
		return true;
	}
	
	@Override
	public void close() {
		try {
			conn.close();
			conn = null;
			Log.info("Connection to the database is now closed.");
		} catch (SQLException e) {
			throwError(e);
		}
	}
}
