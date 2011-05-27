package org.orman.sql;

/**
 * Holds enumeration for table constraints with default string representations
 * according to SQL standard. If a provider is binded then, DBMS-specific
 * grammar can be used.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
public enum TableConstraintType {

	FOREIGN_KEY("FOREIGN KEY (%s) REFERENCES %s (%s)"),
	UNIQUE("UNIQUE (%s)"),
	PRIMARY_KEY("PRIMARY KEY (%s)"),
	AUTO_INCREMENT("AUTO INCREMENT"),
	USING("USING (%s)"),
	NOT_NULL("NOT NULL");

	private String template;
	private static SQLGrammarProvider provider;

	private TableConstraintType(String tpl) {
		this.template = tpl;
	}

	public String getTemplate() {
		if (provider != null)
			return provider.getConstraint(this);
		else
			return this.template;
	}

	public static void setProvider(SQLGrammarProvider p) {
		provider = p;
	}
}
