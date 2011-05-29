package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Required to make a class persistable entity. Use this
 * on the classes that you want to make entities. 
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // works only on class declarations
public @interface Entity {
	String table() default "";
}