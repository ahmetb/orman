package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
public @interface NotNull {

}
