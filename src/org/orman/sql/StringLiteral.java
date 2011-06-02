package org.orman.sql;

public class StringLiteral {
	private static final char surroundChar = '\'';
	
	private String s;
	
	
	public StringLiteral(Object value){
		s = surround(sanitize(value.toString()));
	}
	
	public static String surround(String s){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(surroundChar);
		stringBuilder.append(s);
		stringBuilder.append(surroundChar);
		return stringBuilder.toString();
	}
	
	public static String sanitize(String s){
		// sql injection defence
		s = s.replace(""+surroundChar, ""+surroundChar+surroundChar);
		return s;
	}
	
	@Override
	public String toString() {
		return s;
	}
	
}
