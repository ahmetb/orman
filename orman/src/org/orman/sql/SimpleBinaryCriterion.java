package org.orman.sql;

import org.orman.sql.util.Serializer;


public class SimpleBinaryCriterion extends Criterion {
	private static final String NULL_TOKEN = "NULL";
	
	private String field;
	private String op;
	private String quantifier;
	private String value;
	
	public SimpleBinaryCriterion(String field, BinaryOp op, Object val){
		this.field = field;
		this.op = (op == null ? null : op.toString());
		this.value = (val == null ? NULL_TOKEN : Serializer.serialize(val));
	}
	
	public SimpleBinaryCriterion(String field, BinaryOp op, SetQuantifier quantifier, Object val){
		this(field, op, val);
		this.quantifier = quantifier.toString();
	}
	
	@Override
	public String toString() {
		return (field != null ? field : "")
				+ (op != null ? " "+op+" " : "")
				+ (quantifier != null & value != null ? quantifier+" " : "")
				+ (op != null & value != null ? value : "");
	}
}
