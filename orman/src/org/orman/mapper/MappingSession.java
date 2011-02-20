package org.orman.mapper;

import java.util.LinkedList;
import java.util.Queue;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
import org.orman.mapper.exception.MappingSessionAlreadyStartedException;
import org.orman.mapper.exception.MappingSessionNotStartedException;
import org.orman.mapper.exception.NoDatabaseRegisteredException;
import org.orman.mapper.exception.UnregisteredEntityException;
import org.orman.sql.Query;

/**
 * Mapping session for static system-wide scope. It is statically initialized
 * and entities should register themselves in order to get served.
 * 
 * Provides query execution and transaction managers so that it is a façade
 * class.
 * 
 * @author alp
 * 
 */
public class MappingSession {
	private static PersistenceSchemeMapper scheme;
	private static MappingConfiguration configuration;
	
	private static Database db;
	private static DataTypeMapper typeMapper;
	private static QueryExecutionContainer executer;
	
	private static boolean sessionStarted = false;

	static {
		scheme = new PersistenceSchemeMapper();
		configuration = new MappingConfiguration();
		// implementation, remove soon.
	}
	
	public static void registerDatabase(Database database){
		db = database;
		typeMapper = database.getTypeMapper();
		executer = database.getExecuter();
	}

	/**
	 * Makes physical name and data type bindings to entity and its fields then
	 * registers to the scheme.
	 * 
	 * @param entityClass
	 */
	public static void registerEntity(Class<?> entityClass) {
		Entity e = new Entity(entityClass);

		// BIND TABLE NAME 
		PhysicalNameAndTypeBindingEngine.makeBinding(e, configuration
				.getTableNamePolicy());
		
		scheme.addEntity(e);
	}

	/**
	 * Returns {@link Entity} of given {@link Class}
	 * 
	 * @throws UnregisteredEntityException
	 *             if given <code>entityClass</code> is not found. Be careful
	 *             while using without a unregistered class. This throws
	 *             exception because getEntity() method assumed to be not
	 *             <code>null</code> in the rest of the project.
	 * 
	 */
	public static Entity getEntity(Class<?> entityClass) {
		if (!sessionStarted)
			throw new MappingSessionNotStartedException();
		
		Entity e = scheme.getBindedEntity(entityClass);

		if (e == null)
			throw new UnregisteredEntityException(entityClass.getName());

		return e;
	}
	
	public static boolean entityExists(Class<?> entityClass) {
		if (!sessionStarted)
			throw new MappingSessionNotStartedException();
		
		try {
			getEntity(entityClass);
			return true;
		} catch (UnregisteredEntityException e) {
			return false;
		}
	}

	/**
	 * Returns {@link Entity} of given physical table name.
	 * 
	 * @param tableName
	 *            Caution: case-sensitive
	 * @return
	 */
	public static Entity getEntityByTableName(String tableName) {
		if (!sessionStarted)
			throw new MappingSessionNotStartedException();
		
		return scheme.getEntityByTableName(tableName);
	}

	/**
	 * Return {@link Entity} of given simple class name.
	 * 
	 * WARNING: Be cautious when two {@link Entity} have the same simple class
	 * name e.g: com.app.model.User and com.app.model.administrative.User.
	 * 
	 * If more than occurrences found with same name, return value will be
	 * arbitrarily chosen.
	 * 
	 * @param className
	 *            last part of a class name (from which the table name is
	 *            generated)
	 * @return
	 */
	/*
	 * TODO test registering entities with same names but in different packages,
	 * observe their behavior.
	 */
	public static Entity getEntityByClassName(String className) {
		if (!sessionStarted)
			throw new MappingSessionNotStartedException();
		
		return scheme.getEntityByClassName(className);
	}

	/**
	 * Starts the mapping session. Should be called after registering all the
	 * {@link Entity}(s). According to {@link SchemeCreationPolicy}, in
	 * {@link MappingConfiguration}, it may drop and reconstruct all the tables
	 * from scratch.
	 * 
	 */
	public static void start() {
		if (sessionStarted)
			throw new MappingSessionAlreadyStartedException();
		
		if (db == null)
			throw new NoDatabaseRegisteredException();
		
		else sessionStarted = true; // make the session started.
		
		// BIND NAMES AND TYPES FOR FIELDS
		for(Entity e: scheme.getEntities()){
			scheme.checkIdBinding(e);
			for (Field f : e.getFields()) {
				PhysicalNameAndTypeBindingEngine.makeBinding(e, f, configuration
						.getColumnNamePolicy(), typeMapper);
			}
			scheme.checkConflictingFields(e);
		}
		
		// CONSTRUCT DDL SCHEME FINALLY
		constructScheme();
	}

	/**
	 * Prepares DDL queries to create existing scheme from scrats.
	 * 
	 * TODO return them as a list to execute somehow.
	 * TODO drop table first (but catch-all errors if table does
	 * not exist) then create it.
	 */
	private static void constructScheme() {
		Queue<Query> constructionQueries = new LinkedList<Query>();

		SchemeCreationPolicy policy = configuration.getCreationPolicy();
		
		if (policy.equals(SchemeCreationPolicy.CREATE)
				|| policy.equals(SchemeCreationPolicy.UPDATE)) {
			
			for (Entity e : scheme.getEntities()) {
				
				if (policy.equals(SchemeCreationPolicy.CREATE)){
					// DROP TABLE IF EXISTS
					Query dT = DDLQueryGenerator.dropTableQuery(e);
					constructionQueries.offer(dT);
				}

				// CREATE TABLE
				Query cT = DDLQueryGenerator.createTableQuery(e, policy
						.equals(SchemeCreationPolicy.UPDATE));
				constructionQueries.offer(cT);

				// CREATE INDEXES
				for (Field f : e.getFields()) {
					if (f.getIndex() != null) {
						if (policy.equals(SchemeCreationPolicy.CREATE)){
							// DROP INDEX 
							Query dI = DDLQueryGenerator.dropIndexQuery(e, f);
							constructionQueries.offer(dI);
						}
						
						// CREATE INDEX
						Query cI = DDLQueryGenerator.createIndexQuery(e, f, policy
								.equals(SchemeCreationPolicy.UPDATE));
						constructionQueries.offer(cI);
					}
				}
			}
			
			for(Query q : constructionQueries)
				getExecuter().executeOnly(q);
			System.out.println();
		}
	}

	public static MappingConfiguration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(MappingConfiguration configuration) {
		MappingSession.configuration = configuration;
	}

	public static QueryExecutionContainer getExecuter() {
		return executer;
	}
}