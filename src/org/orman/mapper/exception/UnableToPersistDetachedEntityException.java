package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnableToPersistDetachedEntityException extends OrmanMappingException {
	
	public UnableToPersistDetachedEntityException(String type){
		super("Unable to persist detached (non-persistent) entity `%s`. Save the entity with insert() or update() before using it on other queries.", type);
	}
}
