package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.mapper.LoadingPolicy;

/**
 * Place this annotation on columns to create relationship of 1:* cardinality
 * between entities.
 * 
 * These fields are not physically created on the database but populated at the
 * runtime.
 * 
 * @author alp
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	Class<?> on();
	String targetBindingField() default "";
	LoadingPolicy load() default LoadingPolicy.EAGER;
}