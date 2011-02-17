package org.orman.mapper;

import java.lang.reflect.Method;

import org.orman.mapper.annotation.Column;

public class Field {
	private Class<?> clazz;
	private String originalName;
	private String customName;
	private String generatedName;
	private String type;
	private String customType;
	private FieldIndexHolder index;
	private boolean isId = false;
	private boolean nullable = true;
	
	// Reflection fields
	private Method setterMethod;
	private Method getterMethod;

	public Field(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.originalName = name;
		
		if(clazz.isAnnotationPresent(Column.class)){
			String tmpCustomName = clazz.getAnnotation(Column.class).name();
			String tmpCustomType = clazz.getAnnotation(Column.class).type();
			
			this.customName = (tmpCustomName == null || "".equals(tmpCustomName)) ? null : tmpCustomName;
			this.customType = (tmpCustomType == null || "".equals(tmpCustomType)) ? null : tmpCustomType;
		}
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
	
	@Override
	public String toString() {
		return (this.generatedName == null ? this.originalName : this.generatedName) + ":" + this.clazz.getName() + ((this.type != null) ? "("+this.type+")" : "");
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

	public int compareTo(Field f){
		return this.getGeneratedName().compareTo(f.getGeneratedName());
	}
	
	public boolean equals(Field f){
		return this.compareTo(f) == 0;
	}

	public void setIndex(FieldIndexHolder index) {
		this.index = index;
	}

	public FieldIndexHolder getIndex() {
		return index;
	}

	public void makeId(boolean isId) {
		this.isId = isId;
	}

	public boolean isId() {
		return isId;
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
		this.nullable = nullable;
	}

	public boolean isNullable() {
		return nullable;
	}
}
