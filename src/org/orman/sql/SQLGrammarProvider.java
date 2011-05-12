package org.orman.sql;

public interface SQLGrammarProvider {
	/**
	 * Returns query template of given type of implementor DBMS
	 * including  clause fields binded at runtime (surrounded by {})
	 * without trailing semicolons.
	 * 
	 * @param type 
	 */
	public String getTemplate(QueryType type);
}
