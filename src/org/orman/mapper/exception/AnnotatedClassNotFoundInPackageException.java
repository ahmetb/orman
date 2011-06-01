package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;

@SuppressWarnings("serial")
public class AnnotatedClassNotFoundInPackageException extends
		OrmanMappingException {
	public AnnotatedClassNotFoundInPackageException(String packageName) {
		super("Could not find any entity marked class in \'%s\'", packageName);
	}
}
