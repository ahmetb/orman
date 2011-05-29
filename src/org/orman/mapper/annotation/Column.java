package org.orman.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use of this annotation is not mandatory but
 * required when specifying custom dynamic data type
 * or a custom name for the column. 
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column{
	String name() default "";
	
	/**
	 * Column data type (depending on database software),
	 * remove annotation to make it automatically.
	 */
	String type() default "";
}