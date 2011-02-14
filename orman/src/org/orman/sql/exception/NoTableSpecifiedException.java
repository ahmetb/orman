package org.orman.sql.exception;

public class NoTableSpecifiedException extends QueryBuilderException {
	@Override
	public String getMessage() {
		return "You should specify a table before preparing this query.";
	}
}
