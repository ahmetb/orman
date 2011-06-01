package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class MappingSessionNotStartedException extends OrmanMappingException {
	public MappingSessionNotStartedException(){
		super("Mapping session has not been started. Please start() session before using this method.");
	}
}
