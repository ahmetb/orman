package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnableToSaveDetachedInstanceAsFieldException extends OrmanMappingException {
	public UnableToSaveDetachedInstanceAsFieldException(String field, String clazz){
		super("Unable to a save non-persistent (detached) instance on field `%s` of type %s. Save it first if you have not saved yet, or made changes on it.", field, clazz);
	}
}
