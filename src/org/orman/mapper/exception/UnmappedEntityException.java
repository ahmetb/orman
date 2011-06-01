package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnmappedEntityException extends OrmanMappingException {
	public UnmappedEntityException(String s){
		super("No table name has been generated for the following entity: %s", s);
	}
}
