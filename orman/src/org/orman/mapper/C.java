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
	private static String formatField(Field f){
		return f.getGeneratedName();
	}
	
	public static Criterion eq(Field field, Object val) {
		return C.eq(formatField(field), val);
	}
	
	public static Criterion notEq(Field field, Object val) {
		return C.notEq(formatField(field), val);
	}

	public static Criterion gt(Field field, Object val) {
		return C.gt(formatField(field), val);
	}

	public static Criterion geq(Field field, Object val) {
		return C.geq(formatField(field), val);
	}

	public static Criterion lt(Field field, Object val) {
		return C.lt(formatField(field), val);
	}

	public static Criterion leq(Field field, Object val) {
		return C.leq(formatField(field), val);
	}

	public static Criterion like(Field field, Object pattern) {
		return C.like(formatField(field), pattern);
	}

	public static Criterion notLike(Field field, Object pattern) {
		return C.notLike(formatField(field), pattern);
	}

	public static Criterion between(Field field, Object l, Object h) {
		return C.between(formatField(field), l, h);
	}

	public static Criterion notBetween(Field field, Object l, Object h) {
		return C.notBetween(formatField(field), l, h);
	}

	public static Criterion in(Field field, Object val) {
		return C.in(formatField(field), val);
	}

	public static Criterion notIn(Field field, Object val) {
		return C.notIn(formatField(field), val);
	}

}
