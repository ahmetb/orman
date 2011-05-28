package org.orman.sql.util;


public abstract class TypeCastHandler {
	public abstract Object cast(String rawValue);
	public abstract void normalize();
}
