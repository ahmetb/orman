package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.sql.IndexType;

/**
 * Use this annotation to create index on this field.
 * 
 * @author alp
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
	boolean unique() default false;

	String name() default "";

	IndexType type() default IndexType.HASH;
}