package org.orman.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.MappingConfiguration;
import org.orman.mapper.PhysicalNamingPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
	/**
	 * Target type class.
	 * @return
	 */
	Class<?> toType();
	
	/**
	 * If you want to use a join table with a custom name, you can set it here,
	 * otherwise it will be generated using {@link PhysicalNamingPolicy} in
	 * current {@link MappingConfiguration}.
	 * 
	 * @return custom name for join table or empty string (do auto-binding)
	 */
	public String joinTable() default "";
	
	/**
	 * Default option is LAZY.
	 * 
	 * @return loading policy of field
	 */
	LoadingPolicy load() default LoadingPolicy.LAZY;
}
