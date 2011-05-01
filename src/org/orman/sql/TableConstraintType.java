package org.orman.sql;

public enum TableConstraintType {

	FOREIGN_KEY("FOREIGN KEY (%s) REFERENCES %s (%s)"),
	UNIQUE("UNIQUE (%s)"),
	PRIMARY_KEY("PRIMARY KEY (%s)");
	
	private String template;

	private TableConstraintType(String tpl) {
		this.template = tpl;
	}
	
	public String getTemplate() {
		return template;
	}
}
