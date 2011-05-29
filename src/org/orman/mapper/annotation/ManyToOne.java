package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.orman.mapper.LoadingPolicy;

/**
 * <p>
 * Place this annotation on columns to create relationship of *:1 cardinality
 * between entities. It should have type <code>List<?></code>
 * </p>
 * 
 * <p>
 * This creates a non-unique index on this field. If you want to override its
 * name, use {@link Index} annotation after that.
 * </p>
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
// works only on class fields
public @interface ManyToOne {

	/**
	 * Loading policy of this field. Lazy loading is NOT recommended because it
	 * remains <code>null</code> until it is not loaded manually.
	 * 
	 * @see LoadingPolicy
	 */
	LoadingPolicy load() default LoadingPolicy.EAGER;
}