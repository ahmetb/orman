package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class MappingSessionAlreadyStartedException extends OrmanMappingException {
	public MappingSessionAlreadyStartedException(){
		super("Mapping session has already been started. Do not use start method more than once.");
	}
}
