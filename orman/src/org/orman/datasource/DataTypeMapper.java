package org.orman.datasource;

public interface DataTypeMapper {
	public String getTypeFor(Class<?> clazz);
	public Class<?> getClassFor(String type);
	public void setTypeFor(Class<?> clazz, String type);
}