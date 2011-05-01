package org.orman.sql;

public enum SetQuantifier {
	ANY("ANY"),
	ALL("ALL"),
	SOME("SOME");
	
	private String representation;
	
	private SetQuantifier(String s) {
		this.representation = s;
	}

	@Override
	public String toString() {
		return this.representation;
	}
}
