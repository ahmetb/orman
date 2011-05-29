package org.orman.sql;

import org.orman.sql.util.Glue;

/**
 * To hold logically operable two or more criteria.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
public class Criteria extends Criterion {
	
	private LogicalOperator op;
	private boolean isNot;
	private Criterion[] crit;
	
	
	public Criteria(LogicalOperator op, Criterion... criteria){
		this.isNot = false;
		this.crit = criteria;
		this.op = op;
	}
	
	@Override
	public String toString() {
		return (isNot ? "NOT (" : "") + Glue.concat(this.crit, " "+op.toString()+" ")
				+ (isNot ? ")" : "");
	}
	
	public Criteria not(){
		this.isNot = true;
		return this;
	}
	
}
