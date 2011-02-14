package org.orman.sql;

import org.orman.sql.util.Glue;

public class MultipleQuery extends Query {

	private Query[] queries;
	private String op;

	public MultipleQuery(MultiQuerySetOp op, Query... queries) {
		this.op = op.toString();
		this.queries = queries;
	}

	@Override
	public String toString() {
		return Glue.concat(queries, " " + op + " ");
	}

}
