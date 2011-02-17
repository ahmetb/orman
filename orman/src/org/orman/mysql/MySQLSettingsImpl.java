package org.orman.mysql;

import org.orman.datasource.DatabaseSettings;

public class MySQLSettingsImpl implements DatabaseSettings {
	private short port = 3306;
	private String server = "localhost";
	private String username = "";
	private String password = "";
	private String database = "";
	
	public MySQLSettingsImpl(String username, String password, String db){
		this(username, password, db, null, (short) -1);
	}
	
	public MySQLSettingsImpl(String username, String password, String db, String server, short port){
		if (username != null) this.username = username;
		if (password != null) this.password = password;
		if (db != null) this.database = db;
		if (server != null) this.server = server;
		if (port >= 0) this.port = port;
	}

	public short getPort() {
		return port;
	}

	public String getServer() {
		return server;
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
}
