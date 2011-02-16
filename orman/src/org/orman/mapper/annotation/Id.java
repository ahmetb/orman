package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation for an entity once indicating that this is a primary key
 * and auto-increment id field for this entity.
 * 
 * Allowed to use only once in an entity.
 * 
 * This annotation does not require an {@link Index} annotation. It has an
 * {@link Index} by default, however if you need to override it (for uniqueness
 * and name reasons), you can still use {@link Index}.
 * 
 * @author alp
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}