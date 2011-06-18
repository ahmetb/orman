package org.orman.mapper;

import java.util.HashMap;
import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.exception.ReservedKeywordContainedEntityException;
import org.orman.util.logging.Log;

/***
 * Checks entity and field names for DBMS's reserved keywords.
 * 
 * @author oguz kartal
 *
 */

public class EntityNameValidator {
	private static HashMap<Integer,HashMap<String, Boolean>> keywordMap;
	
	private static final String[] mysqlKeywords = {
		"ACCESSIBLE","ADD","ALL","ALTER",
		"AND","AS","ASC","ASENSITIVE",
		"BETWEEN","BIGINT","BINARY","BLOB",
		"BY","CALL","CASCADE","CASE",
		"CHAR","CHARACTER","CHECK","COLLATE",
		"CONDITION","CONSTRAINT","CONTINUE","CONVERT",
		"CROSS","CURRENT_DATE","CURRENT_TIME","CURRENT_TIMESTAMP",
		"CURSOR","DATABASE","DATABASES","DAY_HOUR",
		"DAY_MINUTE","DAY_SECOND","DEC","DECIMAL",
		"DEFAULT","DELAYED","DELETE","DESC",
		"DETERMINISTIC","DISTINCT","DISTINCTROW","DIV",
		"DROP","DUAL","EACH","ELSE",
		"ENCLOSED","ESCAPED","EXISTS","EXIT",
		"FALSE","FETCH","FLOAT","FLOAT4",
		"FOR","FORCE","FOREIGN","FROM",
		"GENERAL","GRANT","GROUP","HAVING",
		"HOUR_MICROSECOND","HOUR_MINUTE","HOUR_SECOND","IF",
		"IGNORE_SERVER_IDS","IN","INDEX","INFILE",
		"INOUT","INSENSITIVE","INSERT","INT",
		"INT2","INT3","INT4","INT8",
		"INTERVAL","INTO","IS","ITERATE",
		"KEY","KEYS","KILL","LEADING",
		"LEFT","LIKE","LIMIT","LINEAR",
		"LOAD","LOCALTIME","LOCALTIMESTAMP","LOCK",
		"LONGBLOB","LONGTEXT","LOOP","LOW_PRIORITY",
		"MASTER_HEARTBEAT_PERIOD","MASTER_SSL_VERIFY_SERVER_CERT","MATCH","MAXVALUE",
		"MEDIUMINT","MEDIUMTEXT","MIDDLEINT","MINUTE_MICROSECOND",
		"MOD","MODIFIES","NATURAL","NOT",
		"NULL","NUMERIC","ON","ONE_SHOT",
		"OPTION","OPTIONALLY","OR","ORDER",
		"OUTER","OUTFILE","PARTITION","PRECISION",
		"PROCEDURE","PURGE","RANGE","READ",
		"READ_WRITE REAL","REFERENCES","REGEXP","RELEASE",
		"REPEAT","REPLACE","REQUIRE","RESIGNAL",
		"RETURN","REVOKE","RIGHT","RLIKE",
		"SCHEMAS","SECOND_MICROSECOND","SELECT","SENSITIVE","SEPARATOR",
		"SHOW","SIGNAL","SLOW","SMALLINT",
		"SPECIFIC","SQL","SQLEXCEPTION","SQLSTATE",
		"SQL_BIG_RESULT","SQL_CALC_FOUND_ROWS","SQL_SMALL_RESULT","SSL",
		"STRAIGHT_JOIN","TABLE","TERMINATED","THEN",
		"TINYINT","TINYTEXT","TO","TRAILING",
		"TRUE","UNDO","UNION","UNIQUE",
		"UNSIGNED","UPDATE","USAGE","USE",
		"UTC_DATE","UTC_TIME","UTC_TIMESTAMP","VALUES",
		"VARCHAR","VARCHARACTER","VARYING","WHEN",
		"WHILE","WITH","WRITE","XOR","ZEROFILL"
	};
	
	private static final String[] sqliteKeywords = {
		"ABORT","ACTION","ADD","AFTER",
		"ALTER","ANALYZE","AND","AS",
		"ATTACH","AUTOINCREMENT","BEFORE","BEGIN",
		"BY","CASCADE","CASE","CAST",
		"COLLATE","COLUMN","COMMIT","CONFLICT",
		"CREATE","CROSS","CURRENT_DATE","CURRENT_TIME",
		"DATABASE","DEFAULT","DEFERRABLE","DEFERRED",
		"DESC","DETACH","DISTINCT","DROP",
		"ELSE","END","ESCAPE","EXCEPT",
		"EXISTS","EXPLAIN","FAIL","FOR",
		"FROM","FULL","GLOB","GROUP",
		"IF","IGNORE","IMMEDIATE","IN",
		"INDEXED","INITIALLY","INNER","INSERT",
		"INTERSECT","INTO","IS","ISNULL",
		"KEY","LEFT","LIKE","LIMIT",
		"NATURAL","NO","NOT","NOTNULL",
		"OF","OFFSET","ON","OR",
		"OUTER","PLAN","PRAGMA","PRIMARY",
		"RAISE","REFERENCES","REGEXP","REINDEX",
		"RENAME","REPLACE","RESTRICT","RIGHT",
		"ROW","SAVEPOINT","SELECT","SET",
		"TEMP","TEMPORARY","THEN","TO",
		"TRIGGER","UNION","UNIQUE","UPDATE",
		"VACUUM","VALUES","VIEW","VIRTUAL", "WHERE"
	};
	
	private static boolean isValid(String name) {
		name = name.toUpperCase();
		HashMap<String,Boolean> hashSlot;
		
		if (!keywordMap.containsKey(name.length()))
			return true;
		
		hashSlot = keywordMap.get(name.length());
		
		return !hashSlot.containsKey(name);
	}
	
	private static boolean validate(Entity e) {
		List<Field> fields;
		
		if (e == null)
			return false;
		
		if (!isValid(e.getOriginalName())) {
			ReservedKeywordContainedEntityException ex =
					new ReservedKeywordContainedEntityException(
							String.format("Entity \"%s\" is a reserved keyword.",e.getOriginalName()));
			
			Log.error(ex.getMessage());
			throw ex;
		}
		
		fields = e.getFields();
		
		for (Field field : fields) {
			if (!isValid(field.getOriginalName())) {
				ReservedKeywordContainedEntityException ex = 
						new ReservedKeywordContainedEntityException(
								String.format("Field \"%s\" of \"%s\" is a reserved keyword.",
										field.getOriginalName(),e.getOriginalName()));
				
				Log.error(ex.getMessage());
				
				throw ex;
			}
		}
		
		return true;
	}
	
	public static void initialize(Database database) {
		keywordMap = new HashMap<Integer,HashMap<String,Boolean>>();
		String[] keywords = null;
		String keyw;
		String typeStr = database.getClass().getName();
		HashMap<String,Boolean> keywMap;
		int li;
		
		li = typeStr.lastIndexOf('.');
		
		if (li != -1)
			typeStr = typeStr.substring(li+1,typeStr.length());
		
		if (typeStr.equals("MySQL"))
			keywords = mysqlKeywords;
		else if (typeStr.equals("SQLite") || typeStr.equals("SQLiteAndroid"))
			keywords = sqliteKeywords;
		else {
			Log.warn("Database \"%s\" unsupported", typeStr);
			return;
		}
		
		for (int i=0;i<keywords.length;i++) {
			keyw = keywords[i];
			
			if (!keywordMap.containsKey(keyw.length())) {
				keywMap = new HashMap<String,Boolean>();
				keywordMap.put(keyw.length(), keywMap);
				keywMap.put(keyw, true);
			}
			else {
				keywMap = keywordMap.get(keyw.length());
				keywMap.put(keyw, true);
			}
		}	
	}
	
	
	public static void validateEntity(Entity e) {
		validate(e);
	}
	
}
