package org.orman.sql;

public class WhereClause implements ISubclause {
	
	private static final String format = "WHERE %s";
	
	private Criterion conditional;
	
	public WhereClause(Criterion criterion){
		this.conditional = criterion;
	}
	
	@Override
	public String getClauseFormat() {
		return format;
	}
	
	@Override
	public String toString() {
		String conditionalRepresentation = conditional.toString();
		return String.format(getClauseFormat(), conditionalRepresentation);
	}
}
