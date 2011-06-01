package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class RedundantPrimaryKeyException extends OrmanMappingException {
	public RedundantPrimaryKeyException(String e){
		super("Redundant @PrimaryKey fields found on entity %s while there is already an auto-increment exists.", e);
	}
}
