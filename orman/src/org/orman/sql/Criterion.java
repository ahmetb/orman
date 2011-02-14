package org.orman.sql;

public abstract class Criterion {
	public abstract String toString();
	
	public String nest(){
		return "("+this.toString()+")";
	}
}
