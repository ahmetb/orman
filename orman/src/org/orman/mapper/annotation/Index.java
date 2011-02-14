package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to create index on this
 * field. 
 * 
 * @author alp
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Index{
	boolean unique() default false;
	String name() default "";
}