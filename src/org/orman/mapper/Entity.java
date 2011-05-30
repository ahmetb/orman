package org.orman.mapper;

import java.lang.reflect.Constructor;
import java.util.List;

import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.mapper.exception.FieldNotFoundException;
import org.orman.mapper.exception.NotAnEntityException;

/**
 * Entity information holder for classes annotated with
 * {@link org.orman.mapper.annotation.Entity}. It holds original class name,
 * generated table name, {@link Field}s of the entity.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
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

	private boolean isSynthetic = false;
	private Class<?>[] syntheticTypes = {};

	/**
	 * Instantiates an information holder class for
	 * {@link org.orman.mapper.annotation.Entity} annotated classes.
	 * 
	 * @param clazz
	 *            class type of the entity.
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

	/**
	 * Creates a synthetic entity instance using information from a
	 * {@link Field}.
	 * 
	 * @param syntheticEntitySource
	 */
	protected Entity(Field syntheticEntitySource) {
		setSynthetic(true);
		Class<?> holderType = syntheticEntitySource.getRawField().getDeclaringClass();
		Class<?> targetType = syntheticEntitySource.getClazz();
		syntheticTypes = new Class<?>[]{holderType, targetType};
		
		// produce a name for syntheticentity by concating holder-target classnames.
		this.originalName = holderType.getSimpleName() + targetType.getSimpleName();
		
		// create two fields regarding holder and target classes.
		// TODO implement
		
		// make custom name binding if specified any on @ManyToMany annotation.
		// if code reaches here, precondition is satisfied: annotation exists.
		ManyToMany mtm = syntheticEntitySource.getAnnotation(ManyToMany.class);
		
		String tmpCustomName = mtm.joinTable();
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
	 * Adds given field to this entity. Created for framework internals.
	 * 
	 * @param f
	 *            new field.
	 */
	public void addField(Field f) {
		fields.add(f);
	}

	/**
	 * @param fieldName
	 *            case-sensitive field name of entity class.
	 * @throws FieldNotFoundException
	 *             if given field is not found in class.
	 * @return found field instance.
	 */
	public Field getFieldByName(String fieldName) {
		return F.f(getType(), fieldName);
	}

	/**
	 * 
	 * Returns Auto-increment {@link PrimaryKey} {@link Field} of this @
	 * {@link Entity}, if it is not found a <code>null</code> is returned.
	 * 
	 * @return first found auto increment primary key field in entity,
	 *         <code>null</code> if not found.
	 * 
	 *         TODO fix multi pk situation, maybe with an autoincrement check.
	 * 
	 */
	public Field getAutoIncrementField() {
		for (Field f : getFields())
			if (f.isAutoIncrement())
				return f;
		return null;
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

	public int getForeignKeyCount() {
		int c = 0;
		for (Field f : fields)
			if (f.isForeignKey())
				c++;
		return c;
	}

	public void setSynthetic(boolean isSynthetic) {
		this.isSynthetic = isSynthetic;
	}

	/**
	 * Represents whether entity is generated as a result of {@link ManyToMany}
	 * relationship or not.
	 * 
	 * @return whether entitiy is synthetic or not.
	 */
	public boolean isSynthetic() {
		return isSynthetic;
	}

	/**
	 * @param syntheticTypes
	 *            array of two elements of source types.
	 */
	public void setSyntheticTypes(Class<?>[] syntheticTypes) {
		this.syntheticTypes = syntheticTypes;
	}

	/**
	 * @return array of two elements of source types. the order is not
	 *         significant.
	 */
	public Class<?>[] getSyntheticTypes() {
		return syntheticTypes;
	}
}
