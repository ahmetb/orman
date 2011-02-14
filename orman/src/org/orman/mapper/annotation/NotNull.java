package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Ensures that this column is not null
 * while saving. 
 * 
 * @author alp
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}
