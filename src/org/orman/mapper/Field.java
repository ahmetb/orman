package org.orman.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.PrimaryKey;

/**
 * Holds field information such as its original name, generated (physical) name,
 * data type, nullability, whether it is a {@link Index} or {@link PrimaryKey}
 * field and getter-setter methods to obtain and set its value during object
 * mapping if it is a non-public field.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
public class Field implements Comparable<Field> {
	private Class<?> clazz;
	private String originalName;
	private String customName;
	private String generatedName;
	private String type;
	private String customType;
	private FieldIndexHolder index;
	private boolean isNullable = true;
	private boolean isList = false;
	private boolean isPrimaryKey = false;
	private boolean isForeignKey = false;
	private boolean isAutoIncrement = false;

	// Reflection fields
	private Method setterMethod;
	private Method getterMethod;
	private java.lang.reflect.Field rawField;

	public Field(Class<?> clazz, java.lang.reflect.Field fieldInstance) {
		this.clazz = clazz;
		this.originalName = fieldInstance.getName();

		/*
		 * if @Column annotation exists, set custom name and type that overrides
		 * physical binding engine.
		 */

		if (fieldInstance.isAnnotationPresent(Column.class)) {
			Column col = fieldInstance.getAnnotation(Column.class);
			String tmpCustomName = col.name();
			String tmpCustomType = col.type();

			this.customName = (tmpCustomName == null || ""
					.equals(tmpCustomName)) ? null : tmpCustomName;
			this.customType = (tmpCustomType == null || ""
					.equals(tmpCustomType)) ? null : tmpCustomType;
		}
	}
	
	/**
	 * Creates field for a synthetic entity with given class.
	 * Postcondition: custom name does not exists, original name
	 * derived from name of the class. Type is binded after
	 * {@link MappingSession} starts.
	 * 
	 * @param clazz
	 */
	protected Field(Class<?> clazz){
		this.clazz = clazz;
		this.originalName = clazz.getSimpleName()+"Id";
	}

	public String getCustomName() {
		return customName;
	}

	public String getCustomType() {
		return customType;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getGeneratedName() {
		return generatedName;
	}

	public void setGeneratedName(String generatedName) {
		this.generatedName = generatedName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Compares two fields using their physical name.
	 */
	public int compareTo(Field f) {
		return this.originalName.compareTo(f.getOriginalName());
	}

	public boolean equals(Field f) {
		return this.getGeneratedName().equals(f.getGeneratedName());
	}

	public void setIndex(FieldIndexHolder index) {
		this.index = index;
	}

	public FieldIndexHolder getIndex() {
		return index;
	}

	public void setSetterMethod(Method setterMethod) {
		this.setterMethod = setterMethod;
	}

	public Method getSetterMethod() {
		return setterMethod;
	}

	public void setGetterMethod(Method getterMethod) {
		this.getterMethod = getterMethod;
	}

	public Method getGetterMethod() {
		return getterMethod;
	}

	public void setNullable(boolean nullable) {
		this.isNullable = nullable;
	}

	/**
	 * @return <code>false</code> if this field is a NOT NULL field. true in
	 *         default case.
	 */
	public boolean isNullable() {
		return isNullable;
	}

	public void setRawField(java.lang.reflect.Field rawField) {
		this.rawField = rawField;
	}

	public java.lang.reflect.Field getRawField() {
		return rawField;
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> ann) {
		return rawField == null ? false : rawField.isAnnotationPresent(ann);
	}

	public <A extends Annotation> A getAnnotation(Class<A> annClass) {
		return rawField == null ? null : rawField.getAnnotation(annClass);
	}

	public void setList(boolean isList) {
		this.isList = isList;
	}

	public boolean isList() {
		return isList;
	}

	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	public boolean isForeignKey() {
		return isForeignKey;
	}

	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
}
