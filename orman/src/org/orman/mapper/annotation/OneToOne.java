package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Place this annotation on columns to create relationship of 1:1 cardinality
 * between entities.
 * 
 * Also creates an {@link Index} on this field, therefore 
 * if you want to give a specific index name, override it with {@link Index}
 * annotation.
 * 
 * @author alp
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToOne {
	String mappedBy() default "";
}
