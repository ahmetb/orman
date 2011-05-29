package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.mapper.LoadingPolicy;

/**
 * TODO discuss: should target binding be supported?
 * 
 * <p>Place this annotation on columns to create relationship of *:1 cardinality
 * between entities. It should have type <code>List<?></code></p>
 * 
 * <p>This creates a non-unique index on this field. If you want to override its name,
 * use {@link Index} annotation after that.</p>
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
	LoadingPolicy load() default LoadingPolicy.EAGER; //eager loading is default!
}