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
 * CAUTION: Do not forget to use {@link ManyToOne} annotation on the target.
 * 
 * @author alp
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	/**
	 * Target type.
	 * @return
	 */
	Class<?> toType();
	
	/**
	 * Target column to store this instance's id.
	 * @return
	 */
	String on();
	
	/**
	 * Optionally, to fill target instance's related field
	 * with this value.
	 */
	String targetBindingField() default "";
	
	LoadingPolicy load() default LoadingPolicy.EAGER;
}