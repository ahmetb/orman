package org.orman.sql;

public class LimitClause implements ISubclause {
	
	private static final String format = "LIMIT %d, %d";
	
	private int count;
	private int start;
	
	public LimitClause(int recordCount){
		this(recordCount, 0);
	}
	
	public LimitClause(int recordCount, int startOffset){
		this.start = startOffset;
		this.count = recordCount;
	}
	
	@Override
	public String getClauseFormat() {
		return format;
	}
	
	@Override
	public String toString() {
		return String.format(getClauseFormat(), start, count);
	}
}
