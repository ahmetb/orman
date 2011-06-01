package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnsupportedPrimaryKeyFieldTypeException extends OrmanMappingException {
	public UnsupportedPrimaryKeyFieldTypeException(String name, String type){
		super("Given type is not supported on @Id field: `%s` in %s.", name, type);
	}
}
