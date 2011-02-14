package org.orman.sql;

public abstract class DataSource {
	public abstract String toString();
	
	public String nest(){
		return "("+this.toString()+")";
	}
}
