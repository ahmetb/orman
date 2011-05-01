package org.orman.sql;

import org.orman.sql.util.Serializer;

public class UnaryCriteria extends Criterion {
	private String op;
	private String nestedQuery;

	public UnaryCriteria(SetUnaryOp op, Query nestedQuery) {
		this.op = op == null ? null : op.toString();
		this.nestedQuery = Serializer.serialize(nestedQuery);
	}

	@Override
	public String toString() {
		return (op != null ? op : "")
				+ (op != null & nestedQuery != null ? " " + nestedQuery + " "
						: "");
	}
}
