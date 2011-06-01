package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnmappedDataTypeException extends OrmanMappingException {
	public UnmappedDataTypeException(String f, String t){
		super("There is no corresponding data type found for column: %s (%s).", f, t);
	}
}
