package org.orman.sql;

public class TableConstraint {

	private TableConstraintType type;
	private Object[] values;
	
	public TableConstraint(TableConstraintType type, Object... values){
		this.type = type;
		this.values = values;
	}
	
	@Override
	public String toString() {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) values[i] = "NULL";
		}
		
		return String.format(type.getTemplate(), values);
	}
}
