package org.orman.mapper;

import org.orman.mapper.annotation.Entity;
import org.orman.sql.Criterion;

/**
 * {@link Criterion} and logical statement generator façade for objective
 * queries. Extends {@link org.orman.sql.C} such that it can handle all
 * {@link Criterion} producing methods and those which work with actual
 * {@link Entity}es and {@link Field}s.
 * 
 * @author alp
 * 
 */
public class C extends org.orman.sql.C {

	public static Criterion eq(Class<?> entityClass, String fieldName,
			Object val) {
		return C.eq(F.formatField(entityClass, fieldName), serialize(val));
	}

	public static Criterion notEq(Class<?> entityClass, String fieldName,
			Object val) {
		return C.notEq(F.formatField(entityClass, fieldName), serialize(val));
	}

	public static Criterion gt(Class<?> entityClass, String fieldName,
			Object val) {
		return C.gt(F.formatField(entityClass, fieldName), val);
	}

	public static Criterion geq(Class<?> entityClass, String fieldName,
			Object val) {
		return C.geq(F.formatField(entityClass, fieldName), val);
	}

	public static Criterion lt(Class<?> entityClass, String fieldName,
			Object val) {
		return C.lt(F.formatField(entityClass, fieldName), val);
	}

	public static Criterion leq(Class<?> entityClass, String fieldName,
			Object val) {
		return C.leq(F.formatField(entityClass, fieldName), val);
	}

	public static Criterion like(Class<?> entityClass, String fieldName,
			Object pattern) {
		return C.like(F.formatField(entityClass, fieldName), pattern);
	}

	public static Criterion notLike(Class<?> entityClass, String fieldName,
			Object pattern) {
		return C.notLike(F.formatField(entityClass, fieldName), pattern);
	}

	public static Criterion between(Class<?> entityClass, String fieldName,
			Object l, Object h) {
		return C.between(F.formatField(entityClass, fieldName), l, h);
	}

	public static Criterion notBetween(Class<?> entityClass, String fieldName,
			Object l, Object h) {
		return C.notBetween(F.formatField(entityClass, fieldName), l, h);
	}

	public static Criterion in(Class<?> entityClass, String fieldName,
			Object set) {
		return C.in(F.formatField(entityClass, fieldName), set);
	}

	public static Criterion notIn(Class<?> entityClass, String fieldName,
			Object set) {
		return C.notIn(F.formatField(entityClass, fieldName), set);
	}

	/**
	 * Returns id if Object is {@link Entity}, remains the same, otherwise.
	 * Shorthand for Model.fieldValueSerializer.
	 * 
	 * Must be using only for eq() notEq().
	 * 
	 */
	private static Object serialize(Object o) {
		return Model.fieldValueSerializer(o);
	}

}
