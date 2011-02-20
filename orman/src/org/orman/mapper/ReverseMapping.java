package org.orman.mapper;

import org.orman.datasource.ResultList.ResultRow;

/**
 * Provides reverse mapping engine which can convert {@link ResultRow} objects
 * into instances whose their class definitions extend {@link Model}. (which they
 * are also {@link Entity}.
 * 
 * Has smart and safe type conversions so that returned data is appropriate.
 * 
 * @author alp
 */
public class ReverseMapping {
	public static <E extends Model<?>> E map(ResultRow row, Class<E> type, Entity e){
		
		for(Field f : e.getFields()){
			// read field
			System.out.println(f.getOriginalName() +"-->" + row.getColumn(f.getGeneratedName()));
			
			// do needed conversions.
			
			// instantiate with constructor
			
			// return it.
		}
		
		return null;
	}
}
