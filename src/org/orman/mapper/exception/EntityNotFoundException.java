package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class EntityNotFoundException extends OrmanMappingException {
	public EntityNotFoundException(String type){
		super("The entity type %s could not be found in MappingSession context. Register this @Entity to MappingSession first.", type);
	}
}
