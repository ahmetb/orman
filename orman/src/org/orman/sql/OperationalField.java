package org.orman.sql;

public class OperationalField implements IQueryField {
	private QueryFieldOperation operation;
	private String fieldName;
	private String alias;
	
	public OperationalField(QueryFieldOperation operation, String fieldName){
		this.operation = operation;
		this.fieldName = fieldName;
	}
	
	public OperationalField(QueryFieldOperation operation, String fieldName, String as) {
		this(operation, fieldName);
		this.alias = as;
	}

	@Override
	public String getFieldRepresentation() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format(this.operation.toString(), this.fieldName));
		
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
	public String getFieldName() {
		return this.fieldName;
	}
}
