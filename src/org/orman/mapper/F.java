package org.orman.mapper;

import org.orman.mapper.exception.EntityNotFoundException;
import org.orman.mapper.exception.FieldNotFoundException;

/**
 * Shorthand façade to provide {@link Field} objects quickly in a
 * {@link ModelQuery} context.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
public class F {
	/**
	 * Returns physical name of a field in given {@link Entity} class.
	 */
	public static String field(Class<?> type, String fieldName) {
		return f(type, fieldName).getGeneratedName();
	}

	/**
	 * Returns physical name of a field with its {@link Entity} class table name
	 * concetaned with a dot:
	 * 
	 * e.g. formatField(User.class, "lastName") will be "user.last_name".
	 */
	public static String formatField(Class<?> type, String fieldName) {
		Entity e = MappingSession.getEntity(type);
		return formatField(e, fieldName);
	}

	/**
	 * Returns physical name of a field with its Entity table name concetaned
	 * with a dot: e.g. formatField(userEntity, "lastName") will be
	 * "user.last_name".
	 */
	public static String formatField(Entity e, String fieldName) {
		Field f = getField(e, fieldName);
		return e.getGeneratedName() + "." + f.getGeneratedName();
	}

	/**
	 * Returns {@link Field} instance with given <code>fieldName</code> in given
	 * {@link Entity} <code>e</code>.
	 */
	public static Field getField(Entity e, String fieldName) {
		for (Field f : e.getFields())
			if (f.getOriginalName().equals(fieldName))
				return f;
		return null;
	}

	/**
	 * Only a shorthand for getField(..,..). Use <code>field(..,..)</code>
	 * method to get its physical name practically.
	 */
	public static Field f(Class<?> type, String fieldName) {
		Entity e = MappingSession.getEntity(type);

		if (e == null)
			throw new EntityNotFoundException(type.getName());

		Field f = getField(e, fieldName);

		if (f == null)
			throw new FieldNotFoundException(e.getOriginalName(), fieldName);

		return f;
	}
}