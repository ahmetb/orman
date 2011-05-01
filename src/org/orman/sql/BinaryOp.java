package org.orman.sql;

public enum BinaryOp {
	EQUALS("="),
	NOT_EQUALS("<>"),
	GREATER_THAN(">"),
	GREATER_EQUAL(">="),
	LESS_THAN("<"),
	LESS_EQUAL("<="),
	LIKE("LIKE"),
	NOT_LIKE("NOT LIKE"),
	BETWEEN("BETWEEN"),
	NOT_BETWEEN("NOT BETWEEN"),
	IN("IN"),
	NOT_IN("NOT IN");

	private String representation;

	private BinaryOp(String s) {
		this.representation = s;
	}

	@Override
	public String toString() {
		return this.representation;
	}}
