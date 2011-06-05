package org.orman.sql.exception;

import org.orman.exception.OrmanQueryBuilderException;


@SuppressWarnings("serial")
public class DuplicateSubclauseException extends OrmanQueryBuilderException {
	public DuplicateSubclauseException(String t) {
		super("The following subclause is already existing on this query, cannot duplicate: %s", t);
	}
}
