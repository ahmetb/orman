package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.sql.IndexType;

/**
 * Use this annotation to create index on this field.
 * You can set a custom name or a custom type for the
 * index.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
	boolean unique() default false;

	String name() default "";

	IndexType type() default IndexType.HASH;
}