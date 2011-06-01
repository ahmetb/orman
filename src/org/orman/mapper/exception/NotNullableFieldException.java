package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotNullableFieldException extends OrmanMappingException {
	public NotNullableFieldException(String type, String field){
		super("Trying to save instance with a null value on a @NotNull (not nullable) field: `%s` (%s)", field, type);
	}
}
