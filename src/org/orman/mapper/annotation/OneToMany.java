package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.mapper.LoadingPolicy;

/**
 * <p>Place this annotation on columns to create relationship of 1:* cardinality
 * between entities.</p>
 * 
 * <p>These fields are not physically created on the database but populated at the
 * runtime.</p>
 * 
 * CAUTION: Do not forget to use {@link ManyToOne} annotation on the target.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	/**
	 * Target type class.
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