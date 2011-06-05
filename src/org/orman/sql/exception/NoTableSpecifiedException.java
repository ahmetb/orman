package org.orman.sql.exception;

import org.orman.exception.OrmanQueryBuilderException;

@SuppressWarnings("serial")
public class NoTableSpecifiedException extends OrmanQueryBuilderException {
	public NoTableSpecifiedException(){
		super("You should specify a table before preparing this query.");
	}
}
