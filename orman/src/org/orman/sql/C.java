package org.orman.sql;

import org.orman.sql.util.Serializer;

/**
 * Façade class for criterion operations such as
 * creating simple queries, logical expressions,
 * conjuctions and manual criteria etc.
 * 
 * @author alp
 * 
 */
public class C {
	private static final char QUERY_VALUE_WILDCARD = '?';
	
	public static Criterion custom(String expression){
		return new SimpleBinaryCriterion(expression, null, null);
	}
	
	public static Criterion custom(String expression, Object... values){
		// replace question marks with serialized values.
		
		if (values != null){
			int used = 0;
			
			while(expression.indexOf(QUERY_VALUE_WILDCARD) > -1){
				if(used > values.length-1) {
					break;
				} else {
					String serializedValue = Serializer.serialize(values[used++]);
					expression = expression.replace(QUERY_VALUE_WILDCARD+"", serializedValue);
				}
			}
		}
		return new SimpleBinaryCriterion(expression, null, null);
	}
	
	// LOGICAL CONJUCTIONS 

	public static Criteria and(Criterion... criteria) {
		return new Criteria(LogicalOperator.AND, criteria);
	}

	public static Criteria or(Criterion... criteria) {
		return new Criteria(LogicalOperator.OR, criteria);
	}
	
	// SIMPLE COMPARISON METHODS
	
	public static Criterion eq(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.EQUALS, val);
	}
	
	public static Criterion notEq(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.NOT_EQUALS, val);
	}

	public static Criterion gt(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_THAN, val);
	}

	public static Criterion geq(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_EQUAL, val);
	}

	public static Criterion lt(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_THAN, val);
	}

	public static Criterion leq(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_EQUAL, val);
	}

	public static Criterion like(String field, Object pattern) {
		return new SimpleBinaryCriterion(field, BinaryOp.LIKE, pattern);
	}

	public static Criterion notLike(String field, Object pattern) {
		return new SimpleBinaryCriterion(field, BinaryOp.NOT_LIKE, pattern);
	}

	public static Criterion between(String field, Object l, Object h) {
		return new BetweenCriteria(field, BinaryOp.BETWEEN, l, h);
	}

	public static Criterion notBetween(String field, Object l, Object h) {
		return new BetweenCriteria(field, BinaryOp.NOT_BETWEEN, l, h);
	}

	public static Criterion in(String field, Object val) {
		System.out.println("___"+val);
		return new SimpleBinaryCriterion(field, BinaryOp.IN, val);
	}

	public static Criterion notIn(String field, Object val) {
		return new SimpleBinaryCriterion(field, BinaryOp.NOT_IN, val);
	}
	
	//UNARY SET OPERATIONS
	
	public static Criterion exists(Query nestedQuery) {
		return new UnaryCriteria(SetUnaryOp.EXISTS, nestedQuery);
	}
	
	// NESTED QUANTIFIERS QUERYING
	
	public static Criterion eqAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.EQUALS, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion eqAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.EQUALS, SetQuantifier.ANY, nestedQuery);
	}
	
	public static Criterion notEqAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.NOT_EQUALS, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion notEqAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.NOT_EQUALS, SetQuantifier.ANY, nestedQuery);
	}
	
	public static Criterion gtAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_THAN, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion gtAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_THAN, SetQuantifier.ANY, nestedQuery);
	}
	
	public static Criterion geqAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_EQUAL, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion geqAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.GREATER_EQUAL, SetQuantifier.ANY, nestedQuery);
	}

	public static Criterion ltAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_THAN, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion ltAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_THAN, SetQuantifier.ANY, nestedQuery);
	}
	
	public static Criterion leqAll(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_EQUAL, SetQuantifier.ALL, nestedQuery);
	}
	
	public static Criterion leqAny(String field, Query nestedQuery) {
		return new SimpleBinaryCriterion(field, BinaryOp.LESS_EQUAL, SetQuantifier.ANY, nestedQuery);
	}
}