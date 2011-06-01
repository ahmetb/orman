package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class IndexNotFoundException extends OrmanMappingException {
	public IndexNotFoundException(String name){
		super("Unable to create index from a non-indexed field. Use @Index annotation on the following field: %s", name);
	}
}
