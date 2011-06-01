package org.orman.sql.exception;

@SuppressWarnings("serial")
public class NoTableSpecifiedException extends QueryBuilderException {
	public NoTableSpecifiedException(){
		super("You should specify a table before preparing this query.");
	}
}
