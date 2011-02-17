package org.orman.mapper;

import org.orman.mapper.exception.EntityNotFoundException;
import org.orman.mapper.exception.FieldNotFoundException;
import org.orman.mapper.exception.GenericOrmanException;
import org.orman.sql.Criterion;
import org.orman.sql.GroupByClause;
import org.orman.sql.HavingClause;
import org.orman.sql.ISubclause;
import org.orman.sql.JoinType;
import org.orman.sql.MultiQuerySetOp;
import org.orman.sql.MultipleQuery;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;
import org.orman.sql.SubclauseType;

public class ModelQuery {
	
	private QueryBuilder qb; 
	
	private ModelQuery(QueryType type){
		qb = QueryBuilder.getBuilder(type);
	}
	
	public static ModelQuery type(QueryType type){
		return new ModelQuery(type);
	}
	
	public ModelQuery from(Class<?>... type) {
		for(Class<?> c: type)
			this.fromAs(c, null);
		return this;
	}
	
	public ModelQuery fromAs(Class<?> type, String as) {
		Entity e = MappingSession.getEntity(type);
		if (as == null)
			qb.from(e.getGeneratedName());
		else
			qb.fromAs(e.getGeneratedName(), as);
		return this;
	}
	
	public ModelQuery set(Field f, Object value){
		this.qb.set(f.getGeneratedName(), value);
		return this;
	}
	
	public ModelQuery limit(int recordCount){
		return this.limit(recordCount, 0);
	}
	
	public ModelQuery limit(int recordCount, int startOffset) {
		this.qb.limit(recordCount, startOffset);
		return this;
	}
	
	/**
	 * Fields should be in format of EntityName.fieldName,
	 * they will be translated to physical names.
	 * Field expression can be predeced with (+)
	 * indicating ASC, (-) indicating DESCending
	 * order.
	 * 
	 * <p>examples:</p>
	 *  orderBy("User.lastName", "-Customer.id")
	 */
	public ModelQuery orderBy(String... fields){
		for(int i = 0 ; i < fields.length; i++){
			String expr = fields[i].trim();
			boolean desc = fields[i].charAt(0) == '-';
			expr = expr.replace("+", "").replace("-", "");
			
			String fieldName = parseField(expr).getGeneratedName();

			fields[i] = (desc ? "-" : "") + fieldName; 
		}
		
		qb.orderBy(fields);
		return this;
	}

	/**
	 * Fields should be in format of EntityName.fieldName,
	 * they will be translated to physical names.
	 * 
	 * <p>examples:</p>
	 *  groupBy("User.lastName", "Customer.id")
	 */
	public ModelQuery groupBy(String... by){
		for(int i = 0; i < by.length; i++){
			by[i] = parseField(by[i]).getGeneratedName();
		}
		
		this.qb.groupBy(by);
		return this;
	}
	
	
	public ModelQuery having(Criterion c){
		this.qb.having(c);
		return this;
	}
	
	public ModelQuery where(Criterion c){
		this.qb.where(c);
		return this;
	}
	
	public ModelQuery join(Class<?> type){
		return this.join(JoinType.JOIN, type);
	}
	
	public ModelQuery join(JoinType t, Class<?> type){
		return this.join(t, type, null);
	}
	
	public ModelQuery join(JoinType t, Class<?> type, Criterion on){
		this.qb.join(t, MappingSession.getEntity(type).getGeneratedName(), on);
		return this;
	}
	
	
	/*
	 * Naive methods for multi-query set operations:
	 * union, except, intersect.
	 */
	
	public Query union(Query... queries){
		return qb.union(queries);
	}
	
	public Query unionAll(Query... queries){
		return qb.unionAll(queries);
	}
	
	public Query intersect(Query... queries){
		return qb.intersect(queries);
	}
	
	public Query intersectAll(Query... queries){
		return qb.intersectAll(queries);
	}
	
	public Query except(Query... queries){
		return qb.except(queries);
	}
	
	public Query exceptAll(Query... queries){
		return qb.except(queries);
	}

	
	/*
	 * Helper methods:
	 */
	
	private Field parseField(String expr) {
		int s = expr.indexOf('.');

		if (s < 0)
			throw new GenericOrmanException(String.format(
					"Invalid field format: `%s`. (e.g. EntityName.fieldName)",
					expr));
		
		String entityName = expr.substring(0, s);
		String fieldName = expr.substring(s+1, expr.length());
		
		Entity e = MappingSession.getEntityByClassName(entityName);
		
		if (e == null)
			throw new EntityNotFoundException(entityName);
		
		Field f = F.getField(e, fieldName);
		
		if (f == null)
			throw new FieldNotFoundException(e.getOriginalFullName(), fieldName);
		
		return f;
	}

	public Query getQuery(){
		return qb.getQuery();
	}
}
