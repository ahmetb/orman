package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.mapper.LoadingPolicy;

/**
 * TODO protote this entity
 * TODO make sure that this entity is used when manytoone occurs, before starting session.
 * Place this annotation on columns to create relationship of *:1 cardinality
 * between entities.
 * 
 * @author alp
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
	String targetBindingField() default "";
	LoadingPolicy load() default LoadingPolicy.EAGER;
}