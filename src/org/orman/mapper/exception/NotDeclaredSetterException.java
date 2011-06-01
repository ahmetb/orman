package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotDeclaredSetterException extends OrmanMappingException {
	public NotDeclaredSetterException(String field, String clazz){
		super("There is no single-argument setter method declared for non-public field `%s` in %s.", field, clazz);
	}
}
