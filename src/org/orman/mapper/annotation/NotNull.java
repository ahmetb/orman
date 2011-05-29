package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.orman.mapper.exception.NotNullableFieldException;

/**
 * Ensures that this column is not null while saving
 * throws {@link NotNullableFieldException} before saving
 * if this column of the instance is null on insert and
 * update operations. 
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // works only on class fields
public @interface NotNull {
}
