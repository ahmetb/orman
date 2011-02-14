package org.orman.sql;

import org.orman.sql.util.Serializer;

/**
 * Used commonly for BETWEEN clause.
 * 
 * @author alp
 * 
 */
public class BetweenCriteria extends Criterion {
	private String field;
	private String l, h;
	private String op;

	public BetweenCriteria(String field, BinaryOp op, Object low, Object high) {
		this.field = field;
		this.op = op == null ? null : op.toString();
		this.l = Serializer.serialize(l);
		this.h = Serializer.serialize(h);
	}

	@Override
	public String toString() {
		return (field != null ? field : "")
				+ (op != null & l != null ? op : "") + (l != null ? l : "")
				+ "AND" + (h != null ? h : "");
	}
}
