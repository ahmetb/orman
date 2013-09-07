package org.orman.sql;

public enum JoinType {
	JOIN("JOIN"),
	NATURAL_JOIN("NATURAL JOIN"),
	INNER_JOIN("INNER JOIN"),
	CROSS_JOIN("CROSS JOIN"),
	EQUI_JOIN("EQUI JOIN"),
	LEFT_JOIN("LEFT JOIN"),
	RIGHT_JOIN("RIGHT JOIN"),
	FULL_JOIN("FULL JOIN"),
	LEFT_OUTER_JOIN("LEFT OUTER JOIN"),
	RIGHT_OUTER_JOIN("RIGHT OUTER JOIN"),
	FULL_OUTER_JOIN("FULL OUTER JOIN");
	
	
	private String repr;

	private JoinType(String s) {
		this.repr = s;
	}
	
	@Override
	public String toString() {
	        return this.repr;	
	}

}

