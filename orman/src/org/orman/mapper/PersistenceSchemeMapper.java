package org.orman.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.orman.mapper.exception.DuplicateColumnNamesException;
import org.orman.mapper.exception.DuplicateTableNamesException;
import org.orman.mapper.exception.UnmappedDataTypeException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;

public class PersistenceSchemeMapper {
	private Set<Entity> entities;
	private Map<String, Entity> tableNames; // no need for DoubleAssociativeMap
	
	public PersistenceSchemeMapper(){
		this.entities = new HashSet<Entity>();
		this.tableNames = new HashMap<String, Entity>();
	}
	
	/**
	 * Precondition: Name should be binded, otherwise {@link UnmappedEntityException}
	 */
	public void addEntity(Entity e){
		if(e.getGeneratedName() == null || "".equals(e.getGeneratedName()))
			throw new UnmappedEntityException(e.getOriginalFullName());
		
		checkConflictingEntities(e); // exception stops the check.

		this.entities.add(e);
		this.tableNames.put(e.getGeneratedName(), e);
	}
	
	
	public Entity getEntityByTableName(String tblName){
		return this.tableNames.get(tblName);
	}
	
	public Entity getBindedEntity(Class<?> entityClass){
		for(Entity e : getEntities())
			if(e.getClazz().equals(entityClass)) return e;
		return null;
	}
	
	private boolean checkConflictingEntities(Entity e) {
		for(Entity f: this.entities){
			if( e != f  && e.equals(f)){
				throw new DuplicateTableNamesException(f.getOriginalName(), e.getOriginalName());
			}
		}
		return true;
	}
	

	public Set<Entity> getEntities(){
		return this.entities;
	}


	/**
	 * Precondition: Name and type should be binded before,
	 * otherwise {@link UnmappedFieldException}
	 */
	public void checkConflictingFields(Entity e) {
		for(Field f : e.getFields()){
			// unbinded column name
			if (f.getGeneratedName() == null || "".equals(f.getGeneratedName()))
				throw new UnmappedFieldException(f.getOriginalName());
			
			
			// unbinded column data type
			if (f.getType() == null){
				throw new UnmappedDataTypeException(f.getOriginalName(), f.getClazz().getName());
			}
			
			// conflicting column name bindings			
			for(Field g: e.getFields()){
				if (f !=g && f.equals(g)){
					throw new DuplicateColumnNamesException(f.getOriginalName(), g.getOriginalName());
				}
			}
		}
	}
	
}
