package org.orman.mapper;

import org.orman.mapper.exception.EntityNotFoundException;
import org.orman.mapper.exception.FieldNotFoundException;

/**
 * Shorthand façade to provide {@link Field} objects quickly.
 * 
 * @author alp
 *
 */
public class F {
	public static String field(Class<?> type, String fieldName){
		return f(type, fieldName).getGeneratedName();
	}
	
	public static Field getField(Entity e, String fieldName){
		for(Field f :e.getFields())
			if (f.getOriginalName().equals(fieldName)) return f;
		return null;
	}
	
	/**
	 * Only a shorthand for getField(..,..)
	 */
	public static Field f(Class<?> type, String fieldName){
		Entity e = MappingSession.getEntity(type);
		
		if (e == null)
			throw new EntityNotFoundException(type.getName());
		
		Field f = getField(e, fieldName);
		
		if (f == null)
			throw new FieldNotFoundException(e.getOriginalName(), fieldName);
		
		return f;
	}
}