package org.orman.datasource;

/**
 * Maps primitive data types, their wrappers and literals to actual column types
 * according to DBMS.
 * 
 * @author alp
 * 
 */
public interface DataTypeMapper {
	/**
	 * Can be used to obtain {@link String} expression of column type of given
	 * class.
	 * 
	 * @return null if not found
	 */
	public String getTypeFor(Class<?> clazz);

	/**
	 * Finds Java {@link Class} for given data type. Be cautious if you define
	 * data types with some parameters.
	 * 
	 * e.g if you defined {@link String} as "VARCHAR(255)", then requesting
	 * "VARCHAR" won't return {@link String} class.
	 * 
	 */

	public Class<?> getClassFor(String type);

	/**
	 * Allows users to set data types for various classes at runtime. Most
	 * probably won't be needed since all primitive types and wrappers are
	 * already defined statically in implementation of this class.
	 */
	public void setTypeFor(Class<?> clazz, String type);
}