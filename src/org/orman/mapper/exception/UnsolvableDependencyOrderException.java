package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class UnsolvableDependencyOrderException extends OrmanMappingException {
	public UnsolvableDependencyOrderException(){
		super("Dependency graph of your persistence schema contains cycles in the graph. Avoid cycles to create a schema.");
	}
}
