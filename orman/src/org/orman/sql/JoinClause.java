package org.orman.sql;

/*
 * TODOs:
  <join specification>    ::=   <join condition> | <named columns join> 
  <join condition>    ::=   ON <search condition>
  <named columns join>    ::=   USING <left paren> <join column list> <right paren>
  <join column list>    ::=   <column name list>
  
   
 */
public class JoinClause implements ISubclause {
	
	private static final String format = "%s %s %s"; // "{JOIN_TYPE} {TABLE} {ON}
	
	private static final String ON_FORMAT = "ON %s";
	private static final String USING_FORMAT = "USING(%s)";
	
	private Criterion on;
	private Table table;
	private JoinType type;
	
	public JoinClause(JoinType type, String table, Criterion on){
		this(type, new Table(table), on);
	}
	
	public JoinClause(JoinType type, Table table, Criterion on){
		this.type = type;
		this.table = table;
		this.on = on;
	}
	
	@Override
	public String getClauseFormat() {
		return format;
	}
	
	@Override
	public String toString() {
		String joinOnClause = on.toString();
		if(joinOnClause != null) 
			joinOnClause = String.format(this.type != JoinType.EQUI_JOIN ? ON_FORMAT : USING_FORMAT,  joinOnClause);
		
		String typeRepr = (this.type != null) ? this.type.toString() : "";
		
		String tableRepr = table.toString();
		
		return String.format(getClauseFormat(), typeRepr, tableRepr, joinOnClause);
	}
	
	public static void main(String[] args) {
		int [] a = {1,2,3};
		Query q = (QueryBuilder.select().from(
				"sailors").where(
						C.and(
								C.notIn("uname", a)
						).not()).getQuery());
		
		System.out.println(q);
	}
}
