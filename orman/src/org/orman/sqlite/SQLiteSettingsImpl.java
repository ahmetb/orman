package org.orman.sqlite;

import org.orman.datasource.DatabaseSettings;

public class SQLiteSettingsImpl implements DatabaseSettings {
	private String filePath;
	
	public SQLiteSettingsImpl(String fileName){
		setFilePath(fileName);
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
}
