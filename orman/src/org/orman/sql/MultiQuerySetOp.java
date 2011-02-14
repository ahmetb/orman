package org.orman.sql;

public enum MultiQuerySetOp {
	UNION("UNION"),
	UNION_ALL("UNION ALL"),
	INTERSECT("INTERSECT"),
	INTERSECT_ALL("INTERSECT"),
	EXCEPT("EXCEPT"),
	EXCEPT_ALL("EXCEPT ALL");
	
	private String representation;
	
	private MultiQuerySetOp(String s){
		this.representation = s;
	}
	
	@Override
	public String toString() {
		return this.representation;
	}
}
