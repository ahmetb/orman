package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;

@SuppressWarnings("serial")
public class ReservedKeywordUsedException extends OrmanMappingException {
	
	public ReservedKeywordUsedException(String field, String entity, String dbmsClassName) {
		super("Field name or keyword \"%s\" of \"%s\" is a reserved keyword " +
				"on %s. Choose another keyword.", field, entity, dbmsClassName);
	}
}
