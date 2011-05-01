package org.orman.mysql;

import java.sql.Date;

import org.orman.datasource.DataTypeMapper;
import org.orman.util.DoubleAssociativeMap;

public class DataTypeMapperImpl implements DataTypeMapper {

	private static DoubleAssociativeMap<Class<?>, String> typeMap = new DoubleAssociativeMap<Class<?>, String>();
	static {
		typeMap.put(String.class, "LONGTEXT");
		typeMap.put(Character.TYPE, "CHAR");
		typeMap.put(Short.TYPE, "MEDIUMINT(9)");
		typeMap.put(Integer.TYPE, "INT(11)");
		typeMap.put(Long.TYPE, "BIGINT(20)");
		typeMap.put(Boolean.TYPE, "TINYINT(1)");
		typeMap.put(Double.TYPE, "DOUBLE");
		typeMap.put(Float.TYPE, "FLOAT");
		typeMap.put(Date.class, "DATETIME");
		typeMap.put(java.util.Date.class, "DATETIME");
	}

	@Override
	public Class<?> getClassFor(String type) {
		return typeMap.getByVal(type);
	}

	@Override
	public String getTypeFor(Class<?> clazz) {
		return typeMap.getByKey(clazz);
	}

	@Override
	public void setTypeFor(Class<?> clazz, String type) {
		typeMap.put(clazz, type);
	}
}
