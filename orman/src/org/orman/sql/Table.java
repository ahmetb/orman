package org.orman.sql;

public class Table {
	private String name;
	private String alias;
	
	public Table(String name) {
		this.name = name;
	}
	
	public Table(String name, String handle) {
		this(name);
		this.alias = handle;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHandle() {
		return alias;
	}
	
	public void setHandle(String handle) {
		this.alias = handle;
	}
	
	@Override
	public String toString() {
		return name + ((alias == null) ? "" : " as " + alias); 
	}
}
