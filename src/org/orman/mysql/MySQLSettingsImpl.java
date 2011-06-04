package org.orman.mysql;

import org.orman.datasource.DatabaseSettings;

public class MySQLSettingsImpl implements DatabaseSettings {
	private int port = 3306;
	private String host = "localhost";
	private String username = "";
	private String password = "";
	private String database = "";
	private boolean autoCommit = true;
	
	public MySQLSettingsImpl(String username, String password, String db){
		this(username, password, db, null, (short) -1);
	}
	
	public MySQLSettingsImpl(String username, String password, String db, String host){
		this(username, password, db, host, (short) -1);
	}
	
	public MySQLSettingsImpl(String username, String password, String db, String host, int port){
		if (username != null) this.username = username;
		if (password != null) this.password = password;
		if (db != null) this.database = db;
		if (host != null) this.host = host;
		if (port >= 0) this.port = port;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDatabase() {
		return database;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setPort(int port) {
		this.port = port<0 || port>0xffff ? -1 : port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
}
