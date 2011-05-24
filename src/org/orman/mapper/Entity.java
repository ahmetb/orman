package org.orman.mapper;

import java.lang.reflect.Constructor;
import java.util.List;

import org.orman.mapper.annotation.Id;
import org.orman.mapper.exception.NotAnEntityException;
import org.orman.mapper.exception.NotDeclaredIdException;

/**
 * Entity information holder for classes annotated with
 * {@link org.orman.mapper.annotation.Entity}. It holds original class name,
 * generated table name, {@link Field}s of the entity.
 * 
 * @author alp
 * 
 */
public class Entity {
	private Class<?> clazz;
	private List<Field> fields;
	private String originalName;
	private String originalFullName;
	private String customName;
	private String generatedName;
	
	// Reflection fields
	private Constructor<?> defaultConstructor; 
	
	/**
	 * Instantiates an information holder class for
	 * {@link org.orman.mapper.annotation.Entity} annotated classes.
	 * 
	 * @param clazz class type of the entity.
	 */
	public Entity(Class<?> clazz) {
		if (!clazz
				.isAnnotationPresent(org.orman.mapper.annotation.Entity.class))
			throw new NotAnEntityException(clazz.getName()); // require @Entity

		this.clazz = clazz;
		this.originalName = clazz.getSimpleName();
		this.originalFullName = clazz.getName();
		
		// make use of EntityInspector
		EntityInspector ei = new EntityInspector(clazz);
		this.fields = ei.getFields();
		this.setDefaultConstructor(ei.getDefaultConstructor());

		// make custom name binding if specified any on @Entity annotation.
		String tmpCustomName = clazz.getAnnotation(
				org.orman.mapper.annotation.Entity.class).table();
		this.customName = (tmpCustomName == null || "".equals(tmpCustomName)) ? null
				: tmpCustomName;
	}
	
	public Class<?> getType() {
		return clazz;
	}

	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @return {@link Id} {@link Field} of this entity.
	 */
	public Field getIdField() {
		for (Field f : getFields())
			if (f.isId())
				return f;
		throw new NotDeclaredIdException(this.getOriginalFullName());
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getOriginalFullName() {
		return originalFullName;
	}

	public String getGeneratedName() {
		return generatedName;
	}

	public void setGeneratedName(String generatedName) {
		this.generatedName = generatedName;
	}

	public String getCustomName() {
		return customName;
	}

	public int compareTo(Entity e) {
		return this.getOriginalName().compareTo(e.getOriginalName());
	}

	public boolean equals(Entity e) {
		return this.getGeneratedName().equals(e.getGeneratedName());
	}

	public void setDefaultConstructor(Constructor<?> defaultConstructor) {
		this.defaultConstructor = defaultConstructor;
	}

	public Constructor<?> getDefaultConstructor() {
		return defaultConstructor;
	}
	
	public int getForeignKeyCount(){
		int c = 0;
		for(Field f : fields)
			if (f.isForeignKey()) c++;
		return c;
	}
}
