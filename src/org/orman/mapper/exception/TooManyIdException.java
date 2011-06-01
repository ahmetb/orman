package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class TooManyIdException extends OrmanMappingException {
	public TooManyIdException(String s1){
		super("Too many @Id annotations used for this entity. Allowed only one: %s", s1);
	}
}
