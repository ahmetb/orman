package org.orman.mapper;

import java.util.List;

import org.orman.mapper.annotation.Id;
import org.orman.mapper.exception.NotAnEntityException;
import org.orman.mapper.exception.NotDeclaredIdException;

import demo.User;

/**
 * Entity information holder for classes annotated with
 * {@link org.orman.mapper.annotation.Entity}. It holds original
 * class name, generated table name, {@link Field}s of the entity.
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

	public Entity(Class<?> clazz) {
		if (!clazz
				.isAnnotationPresent(org.orman.mapper.annotation.Entity.class))
			throw new NotAnEntityException(clazz.getName());

		this.clazz = clazz;
		this.originalName = clazz.getSimpleName();
		this.originalFullName = clazz.getName();
		this.fields = new EntityInspector(clazz).getFields();

		String tmpCustomName = clazz.getAnnotation(
				org.orman.mapper.annotation.Entity.class).table();
		this.customName = (tmpCustomName == null || "".equals(tmpCustomName)) ? null
				: tmpCustomName;
	}

	public Class<?> getClazz() {
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

	public static void main(String[] args) {
		new Entity(User.class);
	}

	public String getCustomName() {
		return customName;
	}

	public int compareTo(Entity e) {
		return this.getGeneratedName().compareTo(e.getGeneratedName());
	}

	public boolean equals(Entity e) {
		return this.compareTo(e) == 0;
	}
}
