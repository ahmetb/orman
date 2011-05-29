package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.orman.mapper.LoadingPolicy;

/**
 * TODO protote this entity
 * TODO make sure that this entity is used when manytoone occurs, before starting session.
 * TODO discuss: should target binding be supported?
 * 
 * Place this annotation on columns to create relationship of *:1 cardinality
 * between entities.
 * 
 * This creates a non-unique index on this field. If you want to override its name,
 * use {@link Index} annotaation after that.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
	LoadingPolicy load() default LoadingPolicy.EAGER;
}