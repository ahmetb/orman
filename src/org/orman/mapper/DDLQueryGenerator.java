package org.orman.mapper;

import java.util.ArrayList;
import java.util.List;

import org.orman.mapper.exception.IndexNotFoundException;
import org.orman.mapper.exception.RedundantPrimaryKeyException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;
import org.orman.sql.IndexType;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;
import org.orman.sql.TableConstraint;
import org.orman.sql.TableConstraintType;

/**
 * Generates DDL (data description language) {@link Query} for given entity
 * schema.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
// TODO implement tableexists(..)? query and then schemagenerationpolicy.
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

		List<Field> primaryKeyFields = new ArrayList<Field>();
		List<TableConstraint> foreignKeys = new ArrayList<TableConstraint>();
		boolean autoIncrementFound = false;
		
		for (Field f : e.getFields()) {
			if (!f.isList() && (f.getGeneratedName() == null || f.getType() == null)) {
				throw new UnmappedFieldException(f.getOriginalName() + " ("
						+ e.getOriginalName() + ")");
			}
			
			if (!f.isList()){ // list columns are not visible on DDL.
				qb.createColumn(f.getGeneratedName(), f.getType(),
						f.isNullable(), f.isPrimaryKey(), f.isAutoIncrement());
			}
			
			if (f.isPrimaryKey()){
				primaryKeyFields.add(f);
				autoIncrementFound |= f.isAutoIncrement();
			}
			
			if (f.isForeignKey()){
				Entity mappedTo = MappingSession.getEntity(f.getClazz());
				foreignKeys.add(new TableConstraint(TableConstraintType.FOREIGN_KEY, f.getGeneratedName(), mappedTo.getGeneratedName(), mappedTo.getAutoIncrementField().getGeneratedName()));
			}
		}
		
		// add Primary Key constraint.
		if (!primaryKeyFields.isEmpty()){
			if (primaryKeyFields.size() > 1 && autoIncrementFound){
				throw new RedundantPrimaryKeyException(e.getOriginalFullName());
			}
			if(!(primaryKeyFields.size() == 1 && primaryKeyFields.get(0)
						.isPrimaryKey())) {
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < primaryKeyFields.size(); i++){
					sb.append(primaryKeyFields.get(i).getGeneratedName());
					if (i != primaryKeyFields.size() - 1) sb.append(", ");
				}
				qb.addConstraint(new TableConstraint(TableConstraintType.PRIMARY_KEY, sb.toString()));
			}
		}
		
		// add foreign keys at the end.
		for(TableConstraint fkC : foreignKeys){
			qb.addConstraint(fkC);
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

		String physicalIndexName = PhysicalNameGenerator
				.format(on.getIndex().name(), MappingSession.getConfiguration()
						.getIndexNamePolicy());
		
		return QueryBuilder
				.getBuilder(type)
				.from(e.getGeneratedName())
				.setIndex(on.getGeneratedName(), physicalIndexName,
						on.getIndex().getType()).getQuery();
	}
	
	/**
	 * Creates CREATE INDEX {@link Query} for given {@link Entity} on given
	 * composite {@link Field}s. Uniqueness and {@link IndexType} of the index
	 * is determined from first element of <code>onCompositeFields</code> list.
	 * (<code>Field.getIndex().getType(), Field.getIndex().getUnique()</code>
	 * 
	 * <p><b>Precondition: <code>onCompositeIndex</code> is non-empty</b></p>
	 * 
	 * @see FieldIndexHolder about index properties.
	 * 
	 * @return null when onCompositeFields is empty.
	 */
	public static Query createCompositeIndexQuery(Entity e,
			List<Field> onCompositeFields, boolean ifNotExists) {
		if (onCompositeFields == null || onCompositeFields.isEmpty())
			return null;
		
		for(Field on : onCompositeFields){
			if (on.getIndex() == null) {
				throw new IndexNotFoundException(on.getOriginalName());
			}
		}
		
		Field firstField = onCompositeFields.get(0); // arrayindexoutofbounds checked above
		
		QueryType type;
		
		if (!ifNotExists){
			type = firstField.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX
					: QueryType.CREATE_INDEX;
		} else {
			type = firstField.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX_IF_NOT_EXISTS
					: QueryType.CREATE_INDEX_IF_NOT_EXISTS;
		}
		
		StringBuilder indexFields = new StringBuilder();
		for(int i = 0 ; i < onCompositeFields.size(); i++){
			indexFields.append(onCompositeFields.get(i).getGeneratedName());
			if (i != onCompositeFields.size()-1) indexFields.append(", ");
		}
		
		return QueryBuilder
				.getBuilder(type)
				.from(e.getGeneratedName())
				.setIndex(indexFields.toString(), compositeIndexPhysicalName(e),
						firstField.getIndex().getType()).getQuery();
	}
	
	/**
	 * Creates DROP INDEX {@link Query} for given {@link Entity} on given
	 * {@link Field}. Uniqueness of the index can be set on {@link Field} method
	 * <code>getIndex().unique(boolean)</code>.
	 */
	public static Query dropIndexQuery(Entity e, Field on) {
		if (on.getIndex() == null) 
			throw new IndexNotFoundException(on.getOriginalName());
		String indexName = PhysicalNameGenerator.format(on.getIndex().name(),
				MappingSession.getConfiguration().getIndexNamePolicy());
		return QueryBuilder.getBuilder(QueryType.DROP_INDEX_IF_EXISTS).from(e.getGeneratedName())
		.setIndex(on.getGeneratedName(), indexName, on.getIndex().getType())
		.getQuery();
	}
	
	/**
	 * Creates DROP INDEX {@link Query} for given {@link Entity} which 
	 * consists of Primary Key fields of that entity. Index name will
	 * be tablename_index.
	 */
	public static Query dropCompositeIndexQuery(Entity e) {
		return QueryBuilder
				.getBuilder(QueryType.DROP_INDEX_IF_EXISTS)
				.from(e.getGeneratedName())
				.setIndex(e.getGeneratedName(), compositeIndexPhysicalName(e),
						IndexType.HASH) // hash is just dummy.
				.getQuery();
	}

	private static String compositeIndexPhysicalName(Entity e) {
		return PhysicalNameGenerator.format(String.format(
				PhysicalNameAndTypeBindingEngine.COMPOSITE_INDEX_FORMAT,
				e.getGeneratedName(),
				PhysicalNameAndTypeBindingEngine.INDEX_POSTFIX), MappingSession
				.getConfiguration().getIndexNamePolicy());
	}
}
