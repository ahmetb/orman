package org.orman.sqlitegeneric;

import org.orman.datasource.DataTypeMapper;
import org.orman.util.DoubleAssociativeMap;

/**
 * {@link http://www.sqlite.org/datatype3.html}
 * 
 * @author alp
 *
 */
public class DataTypeMapperImpl implements DataTypeMapper {

	private static DoubleAssociativeMap<Class<?>, String> typeMap = new DoubleAssociativeMap<Class<?>, String>();
	static {
		typeMap.put(String.class, "TEXT");
		typeMap.put(Character.TYPE, "TEXT");
		typeMap.put(Short.TYPE, "INTEGER");
		typeMap.put(Integer.TYPE, "INTEGER");
		typeMap.put(Long.TYPE, "INTEGER");
		typeMap.put(Boolean.TYPE, "INTEGER");
		typeMap.put(Double.TYPE, "REAL");
		typeMap.put(Float.TYPE, "REAL");
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
