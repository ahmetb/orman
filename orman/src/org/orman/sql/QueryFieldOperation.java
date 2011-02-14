package org.orman.sql;

public enum QueryFieldOperation {
	COUNT("COUNT(%s)"),
	COUNT_DISTINCT("COUNT(DISTINCT %s)"),
	SUM("SUM(%s)"),
	SUM_DISTINCT("SUM(DISTINCT %s)"),
	AVG("AVG(%s)"),
	AVG_DISTINCT("AVG(DISTINCT %s)"),
	MIN("MIN(%s)"),
	MAX("MAX(%s)");
	
	private String representation;
	
	private QueryFieldOperation(String s) {
		this.representation = s;
	}
	
	@Override
	public String toString() {
		return this.representation.toString();
	}
	
}
