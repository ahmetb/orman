package org.orman.sql;

public class OrderByClause implements ISubclause {
	
	private static final String format = "ORDER BY %s";
	
	private static enum ORDER_OPTIONS{
		ASC, DESC; 
	}

	private String[] by;
	
	public OrderByClause(String... by){
		this.by = by;
	}
	
	@Override
	public String getClauseFormat() {
		return format;
	}
	
	public String prepareFieldOrderPolicy(String field){
		field = field.trim();
		
		ORDER_OPTIONS policy = null;
		
		if(field.charAt(0) == '+'){
			policy = ORDER_OPTIONS.ASC;
			field = field.substring(1);
		}else if(field.charAt(0) == '-'){
			policy = ORDER_OPTIONS.DESC;
			field = field.substring(1);
		}
		return field + ((policy != null) ? " "+policy.toString() : ""); 
	}
	
	@Override
	public String toString() {
		StringBuilder order = new StringBuilder();
		
		for(int i = 0 ; i < by.length; i++){
			order.append(prepareFieldOrderPolicy(by[i]));
			
			if (i != by.length-1){
				order.append(", ");
			}
		}
		
		return String.format(getClauseFormat(), order.toString());
	}
}
