package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Target(ElementType.FIELD) // works only on class fields
public @interface OneToMany {
	/**
	 * Target type class.
	 * @return
	 */
	Class<?> toType();
	
	/**
	 * Target field to store this instance's id.
	 * 
	 * @return
	 */
	String on();
	
	/**
	 * Optionally, to fill target instance's related field
	 * with this value.
	 */
	String targetBindingField() default "";
	// TODO implement target binding. (redundant use on().)
	
	/**
	 * Default option is LAZY since it works pretty well
	 * when {@link ManyToOne} is EAGER (default). 
	 * 
	 * @return loading policy of field
	 */
	LoadingPolicy load() default LoadingPolicy.LAZY;
}