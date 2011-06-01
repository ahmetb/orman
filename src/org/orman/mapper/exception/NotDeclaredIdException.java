package org.orman.mapper.exception;

import org.orman.exception.OrmanMappingException;


@SuppressWarnings("serial")
public class NotDeclaredIdException extends OrmanMappingException {
	public NotDeclaredIdException(String s1){
		super( "The following class does not define a @PrimaryKey(autoIncrement=true) field: %s."  +
			"It should define an autoincrement if it is a type of a foreign field in some other Entity.", s1);
	}
}
