package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.orman.sql.IndexType;

/**
 * Use this annotation to create index on this field.
 * You can set a custom name or a custom type for the
 * index.
 * <p>Note: SQLite does not have index types. This setting is
 * discarded upon DDL generation phase if using SQLite.</p>
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // works only on class fields
public @interface Index {
	boolean unique() default false;

	String name() default "";

	IndexType type() default IndexType.HASH;
}