package org.orman.sql.exception;

@SuppressWarnings("serial")
public class NoTableSpecifiedException extends QueryBuilderException {
	@Override
	public String getMessage() {
		return "You should specify a table before preparing this query.";
	}
}
