package org.orman.sql;

import java.util.HashMap;
import java.util.Map;

public enum SubclauseType {
	WHERE, ORDER_BY, GROUP_BY, HAVING, LIMIT, JOIN;
	
	private static Map<String, SubclauseType> lookup = new HashMap<String, SubclauseType>();

	static{
		for(SubclauseType t: values())
		lookup.put(t.toString().toUpperCase(), t);
	}
	
	public static SubclauseType lookup(String clauseName){
		return lookup.get(clauseName.toUpperCase());
	}
}