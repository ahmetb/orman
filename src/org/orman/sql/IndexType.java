package org.orman.sql;

/**
 * Holds enumeration for various index types that can be
 * created on one or more columns. Default template includes
 * keywords for SQL standards. You can set a {@link SQLGrammarProvider}
 * implementation for customization of keywords.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
public enum IndexType {
	BTREE("BTREE"),
	HASH("HASH");
	
	private static SQLGrammarProvider provider;
	private String template;
	
	private IndexType(String t){
		this.template = t;
	}
	
	public String getKeyword() {
		if (provider != null)
			return provider.getIndexType(this);
		else
			return this.template;
	}

	public static void setProvider(SQLGrammarProvider p) {
		provider = p;
	}
}
