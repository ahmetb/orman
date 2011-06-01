package org.orman.mapper.exception;


@SuppressWarnings("serial")
public class NoDatabaseRegisteredException extends RuntimeException {
	public NoDatabaseRegisteredException(){
		super("No database registered to the mapping session. Use registerDatabase() once.");
	}
}
