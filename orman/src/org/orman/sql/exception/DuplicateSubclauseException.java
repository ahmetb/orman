package org.orman.sql.exception;

import org.orman.sql.SubclauseType;

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
