package org.orman.mapper;

import java.util.HashSet;
import java.util.List;

import org.orman.dbms.Database;
import org.orman.mapper.exception.ReservedKeywordUsedException;

/***
 * Checks entity and field names for DBMS's reserved keywords.
 * 
 * @author oguz kartal
 */
public class EntityNameValidator {
	private static HashSet<String> reservedKeywords; 
	
	private static Class<? extends Database> databaseClass;
	
	/**
	 * checks whether given keyword is reserved or not.
	 * 
	 * @return <code>true</code> if not reserved.
	 */
	private static boolean isValid(String name) {
		name = name.toUpperCase();

		return !reservedKeywords.contains(name);
	}
	
	private static boolean validate(Entity e) {
		List<Field> fields;
		
		if (e == null)
			return false;
		
		if (!isValid(e.getGeneratedName())) {
			throw new ReservedKeywordUsedException(e.getGeneratedName(),
					e.getOriginalFullName(), databaseClass.getSimpleName());
		}
		
		fields = e.getFields();
		
		for (Field field : fields) {
			if (!isValid(field.getGeneratedName())) {
				throw new ReservedKeywordUsedException(
						field.getGeneratedName(), e.getOriginalFullName(),
						databaseClass.getSimpleName());
			}
		}
		
		return true;
	}
	
	public static void initialize(Database database) {
		databaseClass = database.getClass();
		reservedKeywords = new HashSet<String>();

		String[] keywords = database.getSQLGrammar().getReservedKeywords();
		for (String kw : keywords){
			reservedKeywords.add(kw);
		}
	}
	
	/**
	 * Validates given entity for reserved keyword validation checking
	 * its own generated table and column names in case-insensitive manner.
	 * 
	 * @throws ReservedKeywordUsedException if entity has <b>inapplicable</b> keywords.
	 */
	public static void validateEntity(Entity e) {
		validate(e);
	}
}
