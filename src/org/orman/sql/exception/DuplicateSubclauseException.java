package org.orman.sql.exception;


@SuppressWarnings("serial")
public class DuplicateSubclauseException extends QueryBuilderException {
	public DuplicateSubclauseException(String t) {
		super("The following subclause is already existing on this query, cannot duplicate: %s", t);
	}
}
