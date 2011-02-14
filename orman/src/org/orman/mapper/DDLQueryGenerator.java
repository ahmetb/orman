package org.orman.mapper;

import org.orman.mapper.exception.IndexNotFoundException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;

import demo.User;

public class DDLQueryGenerator {
	
	public Query createTable(Entity e){
		if (e.getGeneratedName() == null){
			throw new UnmappedEntityException(e.getOriginalFullName());
		}
		
		QueryBuilder qb = QueryBuilder.getBuilder(QueryType.CREATE_TABLE);
		
		qb.from(e.getGeneratedName());
		
		for(Field f: e.getFields()){
			if (f.getGeneratedName() == null || f.getType() == null){
				throw new UnmappedFieldException(f.getOriginalName()+" (" + e.getOriginalName()+")");
			}
			qb.createColumn(f.getGeneratedName(), f.getType());
		}
		
		System.out.println(qb.prepareSql());
		return qb.getQuery();
	}
	
	public static Query dropTable(Entity e){
		return QueryBuilder.getBuilder(QueryType.DROP_TABLE).from(
				e.getGeneratedName()).getQuery();
	}
	
	public static Query createIndex(Entity e, Field on){
		if (on.getIndex() == null){
			throw new IndexNotFoundException(on.getOriginalName());
		}
		
		QueryType type = on.getIndex().unique() ? QueryType.CREATE_UNIQUE_INDEX : QueryType.CREATE_INDEX;
		
		return QueryBuilder.getBuilder(type).from(e.getGeneratedName()).setIndex(on.getGeneratedName(), on.getIndex().name()).getQuery();
	}
	
	public static void main(String[] args) {
		MappingSession s = new MappingSession();
		Entity e = s.registerEntity(User.class);
		Field f = e.getFields().get(2);
		System.out.println(new DDLQueryGenerator().createIndex(e,f));
	}

}
