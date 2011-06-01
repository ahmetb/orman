package org.orman.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orman.mapper.annotation.PrimaryKey;
import org.orman.mapper.exception.DuplicateColumnNamesException;
import org.orman.mapper.exception.DuplicateTableNamesException;
import org.orman.mapper.exception.NotDeclaredIdException;
import org.orman.mapper.exception.TooManyIdException;
import org.orman.mapper.exception.UnmappedDataTypeException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnmappedFieldException;
import org.orman.util.logging.Log;

/**
 * Holds object mapping scheme and their physical table names.  
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 *
 */
public class PersistenceSchemeMapper {
	private List<Entity> entities;
	private Map<String, Entity> tableNames; // no need for DoubleAssociativeMap

	/**
	 * Initializes an empty ORM scheme.
	 */
	public PersistenceSchemeMapper() {
		this.entities = new ArrayList<Entity>();
		this.tableNames = new HashMap<String, Entity>();
	}

	/**
	 * Adds given entity to the mapping scheme.
	 * 
	 * Precondition: Physical table name should be binded.
	 */
	public void addEntity(Entity e) {

		this.entities.add(e);
		this.tableNames.put(e.getGeneratedName(), e);
	}

	/**
	 * Returns entity with given physical table name.
	 * @param tblName case-sensitive physical table name.
	 * @return
	 */
	public Entity getEntityByTableName(String tblName) {
		return this.tableNames.get(tblName);
	}

	/**
	 * Returns {@link Entity} of given class type.
	 * @param entityClass
	 * @return null if not found.
	 */
	public Entity getBindedEntity(Class<?> entityClass) {
		for (Entity e : getEntities())
			if (e.getType().equals(entityClass))
				return e;
		return null;
	}

	/**
	 * Checks whether physical name of {@link Entity} <code>e</code> in usage.
	 * @return <code>true</code> if no conflict found, throws exception otherwise.
	 * @throws DuplicateTableNamesException if an entity with this physical name exists.
	 */
	protected boolean checkConflictingEntities(Entity e) {
		for (Entity f : this.entities) {
			if (e != f && e.equals(f)) {
				throw new DuplicateTableNamesException(f.getOriginalName(), e
						.getOriginalName());
			}
		}
		return true;
	}

	/**
	 * @return all {@link Entity}s in scheme.
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Precondition: Name and type should be binded.
	 * 
	 * @throws UnmappedFieldException
	 *             if physical column name is not binded.
	 * @throws UnmappedDataTypeException
	 *             if physical data type is not binded.
	 * @throws DuplicateColumnNamesException
	 *             if there exists more than one fields with same physical
	 *             column name
	 * 
	 * 
	 */
	public void checkConflictingFields(Entity e) {
		if(e.getFields() != null)
			for (Field f : e.getFields()) {
			// unbinded column name
			if (f.getGeneratedName() == null || "".equals(f.getGeneratedName()))
				throw new UnmappedFieldException(f.getOriginalName());

			// unbinded column data type on a non-entity field or list of a non-entity field.
			if (f.getType() == null && !MappingSession.entityExists(f.getClazz())
					//&& f.getType().equals(List.class) //TODO seems unnecessary
			) {
				throw new UnmappedDataTypeException(f.getOriginalName(), f
						.getClazz().getName());
			}

			// conflicting column name bindings
			for (Field g : e.getFields()) {
				if (f != g && f.equals(g)) {
					throw new DuplicateColumnNamesException(
							f.getOriginalName(), g.getOriginalName());
				}
			}
		}
	}

	/**
	 * Ensures that {@link PrimaryKey} annotation exist exactly once in {@link Field}s
	 * of given {@link Entity}.
	 * 
	 * @throws TooManyIdException
	 *             if exists more than one.
	 * @throws NotDeclaredIdException
	 *             if does not exist any.
	 * @param e
	 */
	//TODO CRITICAL: Decide: Is @PrimaryKey annotation necessary? Can't there exist multiple PKs?
	public void checkPrimaryKeyBinding(Entity e) {
		int pkOccurrenceCount = 0;

		for (Field f : e.getFields()) {
			if (f.isPrimaryKey())
				pkOccurrenceCount++;
		}
		
		if (pkOccurrenceCount < 1)
			throw new NotDeclaredIdException(e.getOriginalFullName());
		else if (pkOccurrenceCount > 1)
			throw new TooManyIdException(e.getOriginalFullName());
	}

	/**
	 * WARNING: Be cautious when two Entities have the same class name. e.g.
	 * com.app.model.User and com.app.model.administrative.User.
	 * 
	 * If more than occurrences found with same name, return value will be
	 * arbitrarily chosen.
	 * 
	 */
	public Entity getEntityByClassName(String className) {
		for(Entity e: getEntities()){
			if (e.getOriginalName().equals(className))
				return e;
		}
		return null;
	}

}
