package org.orman.sql;

import org.orman.sql.util.Glue;

public class GroupByClause implements ISubclause {
	
	private static final String format = "GROUP BY %s";
	
	private String[] by;
	
	public GroupByClause(String... by){
		this.by = by;
	}
	
	@Override
	public String getClauseFormat() {
		return format;
	}
	
	@Override
	public String toString() {
		return String.format(getClauseFormat(), Glue.concatNoEscape(this.by, ", "));
	}
}
