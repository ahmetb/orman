package org.orman.sql;

public enum SetUnaryOp {
	EXISTS("EXISTS"),
	NOT_EXISTS("NOT EXISTS"),
	;
	
	private String representation;
	
	private SetUnaryOp(String s){
		this.representation = s;
	}
	
	@Override
	public String toString() {
		return this.representation;
	}
}
