package org.orman.sql.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.orman.sql.Query;
import org.orman.sql.StringLiteral;


public class Serializer {
	
	private static final String NULL_CONSTANT = "NULL";
	private static final DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String serialize(Object o){
		
		if (o instanceof Query){
			return ((Query) o).nest(); // return nested query
		}
		
		if (o instanceof String){
			return new StringLiteral(o).toString();
		}
		
		if (o instanceof Date){
			return new StringLiteral(dateTimeFormatter.format((Date) o)).toString();
		}

		
		if (o instanceof String[]){
			return "("+Glue.concat((String[]) o, ", ")+")";
		}
		
		if (o instanceof Object[]){
			return "("+Glue.concat((Object[]) o, ", ")+")";
		}
		
		if (o instanceof int[]){
			return "("+Glue.concat((int[]) o, ", ")+")";
		}
		
		if (o instanceof List<?>){
			return "("+Glue.concat((List<?>) o, ", ")+")";
		}
		
		return (o == null)? NULL_CONSTANT : o.toString();
	}
}
