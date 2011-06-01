package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotDeclaredGetterException extends OrmanMappingException {
	public NotDeclaredGetterException(String field, String clazz){
		super("There is no getter method (non-void and without arguments) declared for non-public field `%s` in %s.", field, clazz);
	}
}
