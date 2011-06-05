package org.orman.mapper;

import org.orman.exception.OrmanMappingException;
import org.orman.mapper.exception.EntityNotFoundException;
import org.orman.mapper.exception.FieldNotFoundException;
import org.orman.sql.Criterion;
import org.orman.sql.JoinType;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;

/**
 * Provides a query builder, it is different than SQL {@link QueryBuilder} since
 * it operates on actual {@link Field}s and {@link Entity}s and build queries
 * according to their physical names.
 * 
 * @see QueryBuilder for building queries with physical names.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
public class ModelQuery {

	private QueryBuilder qb;

	private ModelQuery(QueryType type) {
		qb = QueryBuilder.getBuilder(type);
	}

	public static ModelQuery select() {
		return type(QueryType.SELECT);
	}

	public static ModelQuery update() {
		return type(QueryType.UPDATE);
	}

	public static ModelQuery delete() {
		return type(QueryType.DELETE);
	}

	public static ModelQuery insert() {
		return type(QueryType.INSERT);
	}

	public static ModelQuery type(QueryType type) {
		return new ModelQuery(type);
	}
	
	public ModelQuery selectColumn(Class<?> entityClass, String fieldName) {
		this.qb.select(F.formatField(entityClass, fieldName));
		return this;
	}
	
	public ModelQuery selectColumn(Entity e, String fieldName) {
		this.qb.select(F.formatField(e, fieldName));
		return this;
	}

	protected ModelQuery from(Entity... e) {
		for (Entity a : e) {
			this.qb.from(a.getGeneratedName());
		}
		return this;
	}
	
	public ModelQuery from(Class<?>... type) {
		for (Class<?> c : type) {
			Entity e = MappingSession.getEntity(c);
			this.qb.from(e.getGeneratedName());
		}
		return this;
	}
	
	public ModelQuery set(Class<?> entityClass, String fieldName, Object value) {
		this.qb.set(F.f(entityClass, fieldName).getGeneratedName(), value);
		return this;
	}

	public ModelQuery set(Field f, Object value) {
		this.qb.set(f.getGeneratedName(), value);
		return this;
	}

	public ModelQuery count() {
		qb.count();
		return this;
	}
	
	public ModelQuery limit(int recordCount) {
		return this.limit(recordCount, 0);
	}

	public ModelQuery limit(int recordCount, int startOffset) {
		this.qb.limit(recordCount, startOffset);
		return this;
	}

	/**
	 * Fields should be in format of EntityName.fieldName, they will be
	 * translated to physical names. Field expression can be predeced with (+)
	 * indicating ASC, (-) indicating DESCending order.
	 * 
	 * <p>
	 * examples:
	 * </p>
	 * orderBy("User.lastName", "-Customer.id")
	 */
	public ModelQuery orderBy(String... fields) {
		for (int i = 0; i < fields.length; i++) {
			String expr = fields[i].trim();
			boolean desc = fields[i].charAt(0) == '-';
			expr = expr.replace("+", "").replace("-", "");

			Entity e = parseEntity(expr);
			Field f = parseField(expr);
			String fieldName = f.getOriginalName();

			fields[i] = (desc ? "-" : "") + F.formatField(e, fieldName);
		}

		qb.orderBy(fields);
		return this;
	}

	/**
	 * Fields should be in format of EntityName.fieldName, they will be
	 * translated to physical names.
	 * 
	 * <p>
	 * examples:
	 * </p>
	 * groupBy("User.lastName", "Customer.id")
	 */
	public ModelQuery groupBy(String... by) {
		for (int i = 0; i < by.length; i++) {
			by[i] = parseField(by[i]).getGeneratedName();
		}

		this.qb.groupBy(by);
		return this;
	}

	public ModelQuery having(Criterion c) {
		this.qb.having(c);
		return this;
	}

	public ModelQuery where(Criterion c) {
		this.qb.where(c);
		return this;
	}

	public ModelQuery join(Class<?> type) {
		return this.join(JoinType.JOIN, type);
	}

	public ModelQuery join(JoinType t, Class<?> type) {
		return this.join(t, type, null);
	}

	public ModelQuery join(JoinType t, Class<?> type, Criterion on) {
		this.qb.join(t, MappingSession.getEntity(type).getGeneratedName(), on);
		return this;
	}

	/*
	 * Naive methods for multi-query set operations: union, except, intersect.
	 */

	public Query union(Query... queries) {
		return qb.union(queries);
	}

	public Query unionAll(Query... queries) {
		return qb.unionAll(queries);
	}

	public Query intersect(Query... queries) {
		return qb.intersect(queries);
	}

	public Query intersectAll(Query... queries) {
		return qb.intersectAll(queries);
	}

	public Query except(Query... queries) {
		return qb.except(queries);
	}

	public Query exceptAll(Query... queries) {
		return qb.except(queries);
	}

	/*
	 * Helper methods:
	 */

	private Field parseField(String expr) {
		int s = expr.indexOf('.');

		if (s < 0)
			throw new OrmanMappingException(String.format(
					"Invalid field format: `%s`. (e.g. EntityName.fieldName)",
					expr));

		String entityName = expr.substring(0, s);
		String fieldName = expr.substring(s + 1, expr.length());

		Entity e = MappingSession.getEntityByClassName(entityName);

		if (e == null)
			throw new EntityNotFoundException(entityName);

		Field f = F.getField(e, fieldName);

		if (f == null)
			throw new FieldNotFoundException(e.getOriginalFullName(), fieldName);

		return f;
	}

	private Entity parseEntity(String expr) {
		int s = expr.indexOf('.');

		if (s < 0)
			throw new OrmanMappingException(String.format(
					"Invalid field format: `%s`. (e.g. EntityName.fieldName)",
					expr));

		String entityName = expr.substring(0, s);
		Entity e = MappingSession.getEntityByClassName(entityName);
		if (e == null)
			throw new EntityNotFoundException(entityName);
		return e;
	}

	public Query getQuery() {
		return qb.getQuery();
	}
}
