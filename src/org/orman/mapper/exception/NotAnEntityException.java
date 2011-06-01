package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotAnEntityException extends OrmanMappingException {
	public NotAnEntityException(String name){
		super("The following class is not eligible to be an entity. Use @Entity annotation: %s", name);
	}
}
