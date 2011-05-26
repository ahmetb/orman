package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the column is an auto increment field so that it will be
 * generated upon scheme construction. Make sure that this field is an
 * augmentable (numeric) type which your DBMS can handle.
 * 
 * <p>
 * Caution: Auto increment fields are automatically set as primary key even if
 * it is not explicitly declared as with annotation {@link PrimaryKey}.
 * </p>
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrement {
	// no implementation needed.
}