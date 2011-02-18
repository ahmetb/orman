package org.orman.sql.exception;


@SuppressWarnings("serial")
public class DuplicateSubclauseException extends QueryBuilderException {
	private String message = "The following subclause is already existing on this query, cannot duplicate: %s";

	String type;

	public DuplicateSubclauseException(String t) {
		this.type = t;
	}

	@Override
	public String getMessage() {
		return String.format(this.message, this.type);
	}
}
