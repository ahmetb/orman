package org.orman.mapper;

import org.orman.sql.Criterion;

/**
 * Crtieria and logical statement generator
 * façade for objective queries.
 * 
 * @author alp
 *
 */
public class C extends org.orman.sql.C {
	public static Criterion eq(Class<?> entityClass, String fieldName, Object val) {
		return C.eq(F.field(entityClass, fieldName), val);
	}
	
	public static Criterion notEq(Class<?> entityClass, String fieldName, Object val) {
		return C.notEq(F.field(entityClass, fieldName), val);
	}

	public static Criterion gt(Class<?> entityClass, String fieldName, Object val) {
		return C.gt(F.field(entityClass, fieldName), val);
	}

	public static Criterion geq(Class<?> entityClass, String fieldName, Object val) {
		return C.geq(F.field(entityClass, fieldName), val);
	}

	public static Criterion lt(Class<?> entityClass, String fieldName, Object val) {
		return C.lt(F.field(entityClass, fieldName), val);
	}

	public static Criterion leq(Class<?> entityClass, String fieldName, Object val) {
		return C.leq(F.field(entityClass, fieldName), val);
	}

	public static Criterion like(Class<?> entityClass, String fieldName, Object pattern) {
		return C.like(F.field(entityClass, fieldName), pattern);
	}

	public static Criterion notLike(Class<?> entityClass, String fieldName, Object pattern) {
		return C.notLike(F.field(entityClass, fieldName), pattern);
	}

	public static Criterion between(Class<?> entityClass, String fieldName, Object l, Object h) {
		return C.between(F.field(entityClass, fieldName), l, h);
	}

	public static Criterion notBetween(Class<?> entityClass, String fieldName, Object l, Object h) {
		return C.notBetween(F.field(entityClass, fieldName), l, h);
	}

	public static Criterion in(Class<?> entityClass, String fieldName, Object set) {
		return C.in(F.field(entityClass, fieldName), set);
	}

	public static Criterion notIn(Class<?> entityClass, String fieldName, Object set) {
		return C.notIn(F.field(entityClass, fieldName), set);
	}

}
