package org.orman.sql;

public class DataLiteral extends DataSource {
	
	private static final char STRING_SURROUNDER = '\'' ;
	
	private String value;
	
	public DataLiteral(int i){
		value = new Integer(i).toString();
	}
	
	public DataLiteral(long l){
		value = new Long(l).toString();
	}
	
	public DataLiteral(float f){
		value = new Float(f).toString();
	}
	
	public DataLiteral(double d){
		value = new Double(d).toString();
	}
	
	public DataLiteral(boolean b){
		value = new Boolean(b).toString();
	}
	
	public DataLiteral(String s){
		value = STRING_SURROUNDER+s+STRING_SURROUNDER;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
