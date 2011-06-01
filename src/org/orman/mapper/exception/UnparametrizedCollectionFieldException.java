package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;

@SuppressWarnings("serial")
public class UnparametrizedCollectionFieldException extends
		OrmanMappingException {
	public UnparametrizedCollectionFieldException(String s1, String s2) {
		super("Unparametrized collection types (List, ArrayList, LinkedList) are not allowed as persistent field `%s` in %s. Parametrize them with classes of entity types.",
				s1, s2);
	}
}
