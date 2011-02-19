package org.orman.mapper;

import org.orman.mapper.exception.IndexNotFoundException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;

/**
 * Generates DDL (data description language) {@link Query} for given entity
 * scheme.
 * 
 * @author alp
 * 
 */
public class DDLQueryGenerator {

	/**
	 * Creates CREATE TABLE {@link Query} for given {@link Entity}.
	 */
	public static Query createTableQuery(Entity e) {
		if (e.getGeneratedName() == null) {
			throw new UnmappedEntityException(e.getOriginalFullName());
		}

		QueryBuilder qb = QueryBuilder.getBuilder(QueryType.CREATE_TABLE);

		qb.from(e.getGeneratedName());

		for (Field f : e.getFields()) {
			if (f.getGeneratedName() == null || f.getType() == null) {
				throw new UnmappedFieldException(f.getOriginalName() + " ("
						+ e.getOriginalName() + ")");
			}
			qb.createColumn(f.getGeneratedName(), f.getType(), f.isNullable());
		}

		return qb.getQuery();
	}

	/**
	 * Creates DROP TABLE {@link Query} for given {@link Entity}
	 */
	public static Query dropTableQuery(Entity e) {
		return QueryBuilder.getBuilder(QueryType.DROP_TABLE_IF_EXISTS).from(
				e.getGeneratedName()).getQuery();
	}

	/**
	 * Creates CREATE INDEX {@link Query} for given {@link Entity} on given
	 * {@link Field}. Uniqueness of the index can be set on {@link Field} method
	 * <code>getIndex().unique(boolean)</code>.
	 */
	public static Query createIndexQuery(Entity e, Field on) {
		if (on.getIndex() == null) {
			throw new IndexNotFoundException(on.getOriginalName());
		}

		QueryType type = on.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX
				: QueryType.CREATE_INDEX;

		return QueryBuilder.getBuilder(type).from(e.getGeneratedName())
				.setIndex(on.getGeneratedName(), on.getIndex().name())
				.getQuery();
	}
}
