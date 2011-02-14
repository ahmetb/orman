package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Required to make a class persistable entity.
 * 
 * @author alp
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	String table() default "";
}