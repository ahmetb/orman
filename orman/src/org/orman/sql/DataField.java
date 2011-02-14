package org.orman.sql;


public class DataField extends DataSource implements IQueryField {
	private String fieldName;
	private String alias;
	
	public DataField(String fieldName){
		this.fieldName = fieldName;
	}
	
	public DataField(String fieldName, String as) {
		this(fieldName);
		this.alias = as;
	}
	
	@Override
	public String getFieldRepresentation() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.fieldName);
		
		if(alias != null){
			stringBuilder.append(" as ");
			stringBuilder.append(this.alias);
		}
		return stringBuilder.toString();
	}

	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public String toString() {
		return getFieldRepresentation();
	}

}