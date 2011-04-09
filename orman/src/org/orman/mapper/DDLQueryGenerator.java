package org.orman.mapper;

import org.orman.mapper.exception.IndexNotFoundException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;
import org.orman.sql.TableConstraint;
import org.orman.sql.TableConstraintType;

/**
 * Generates DDL (data description language) {@link Query} for given entity
 * scheme.
 * 
 * @author alp
 * 
 */
// TODO implement tableexists(..)? query and then schemegenerationpolicy.
public class DDLQueryGenerator {

	/**
	 * Creates CREATE TABLE {@link Query} for given {@link Entity}.
	 */
	public static Query createTableQuery(Entity e, boolean ifNotExists) {
		if (e.getGeneratedName() == null) {
			throw new UnmappedEntityException(e.getOriginalFullName());
		}

		QueryType qt = (ifNotExists) ? QueryType.CREATE_TABLE_IF_NOT_EXSISTS : QueryType.CREATE_TABLE;
		QueryBuilder qb = QueryBuilder.getBuilder(qt);

		qb.from(e.getGeneratedName());

		for (Field f : e.getFields()) {
			if (!f.isList() && (f.getGeneratedName() == null || f.getType() == null)) {
				throw new UnmappedFieldException(f.getOriginalName() + " ("
						+ e.getOriginalName() + ")");
			}
			
			if (!f.isList()) // list columns are not saved.
				qb.createColumn(f.getGeneratedName(), f.getType(), f.isNullable(), f.isId());
			
			if (f.isForeignKey()){
				Entity mappedTo = MappingSession.getEntity(f.getClazz());
				qb.addConstraint(new TableConstraint(TableConstraintType.FOREIGN_KEY, f.getGeneratedName(), mappedTo.getGeneratedName(), mappedTo.getIdField().getGeneratedName()));
			}
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
	public static Query createIndexQuery(Entity e, Field on, boolean ifNotExists) {
		if (on.getIndex() == null) {
			throw new IndexNotFoundException(on.getOriginalName());
		}

		QueryType type;
		
		if (!ifNotExists){
			type = on.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX
					: QueryType.CREATE_INDEX;
		} else {
			type = on.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX_IF_NOT_EXISTS
					: QueryType.CREATE_INDEX_IF_NOT_EXISTS;
		}

		return QueryBuilder.getBuilder(type).from(e.getGeneratedName())
				.setIndex(on.getGeneratedName(), on.getIndex().name())
				.getQuery();
	}
	
	/**
	 * Creates DROP INDEX {@link Query} for given {@link Entity} on given
	 * {@link Field}. Uniqueness of the index can be set on {@link Field} method
	 * <code>getIndex().unique(boolean)</code>.
	 */
	public static Query dropIndexQuery(Entity e, Field on) {
		if (on.getIndex() == null) 
			throw new IndexNotFoundException(on.getOriginalName());
		
		return QueryBuilder.getBuilder(QueryType.DROP_INDEX_IF_EXISTS).from(e.getGeneratedName())
		.setIndex(on.getGeneratedName(), on.getIndex().name())
		.getQuery();
	}
}
