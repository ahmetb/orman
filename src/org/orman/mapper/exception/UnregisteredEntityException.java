package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnregisteredEntityException extends OrmanMappingException {
	public UnregisteredEntityException(String cl){
		super("Unable to retrieve Entity of an unregistered class. Register the following class to MappingSession as Entity first: %s.", cl);
	}
}
