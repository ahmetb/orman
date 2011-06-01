package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotDeclaredDefaultConstructorException extends OrmanMappingException {
	public NotDeclaredDefaultConstructorException(String s1){
		super("The following class does not define a public default constructor (with no parameters): %s", s1);
	}
}
