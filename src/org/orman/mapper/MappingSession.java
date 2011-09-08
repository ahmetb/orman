package org.orman.mapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.orman.dbms.DataTypeMapper;
import org.orman.dbms.Database;
import org.orman.dbms.QueryExecutionContainer;
import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.exception.AnnotatedClassNotFoundInPackageException;
import org.orman.mapper.exception.DuplicateTableNamesException;
import org.orman.mapper.exception.MappingSessionAlreadyStartedException;
import org.orman.mapper.exception.MappingSessionNotStartedException;
import org.orman.mapper.exception.NoDatabaseRegisteredException;
import org.orman.mapper.exception.UnmappedEntityException;
import org.orman.mapper.exception.UnregisteredEntityException;
import org.orman.sql.IndexType;
import org.orman.sql.Query;
import org.orman.sql.QueryType;
import org.orman.sql.SQLGrammarProvider;
import org.orman.sql.TableConstraintType;
import org.orman.util.logging.Log;

/**
 * Mapping session for static system-wide scope. It is statically initialized
 * and entities should register themselves in order to get served.
 * 
 * Provides query execution and transaction managers so that it is a façade
 * class.
 * 
 * @author ahmet alp balkan
 * @author oguz kartal
 */
public class MappingSession {
	private static PersistenceSchemaMapper schema;
	private static MappingConfiguration configuration;

	private static Database db;
	private static DataTypeMapper typeMapper;
	private static QueryExecutionContainer executer;

	private static boolean sessionStarted = false;
	private static boolean autoPackageRegistration = true;

	static {
		schema = new PersistenceSchemaMapper();
		configuration = new MappingConfiguration();
		// implementation, remove soon.
	}

	public static void registerDatabase(Database database) {
		db = database;
		typeMapper = database.getTypeMapper();
		executer = database.getExecuter();
		
		// register grammar to reserved keyword checker
		EntityNameValidator.initialize(database);
	}
	/**
	 * Finds and registers @{@link Entity}-annotated classes in given package,
	 * recursively. See also <code>registerEntity(Class<?>)</code> method for
	 * more.
	 * 
	 * <p>Note: (postcondition) disables auto-package scanning for entities.</p>
	 * 
	 * @param packageName
	 *            Entity container package name. e.g. com.my.entities
	 * @throws MappingSessionAlreadyStartedException
	 *             if session is already started.
	 * @throws AnnotatedClassNotFoundInPackageException
	 *             if no classes have been found in given package name, because
	 *             assuming you think you have some. Helps preventing incorrect
	 *             package name usage.
	 */
	public static void registerPackage(String packageName) {
		
		if (db == null) {
			throw new NoDatabaseRegisteredException();
		}
		
		List<Class<?>> annotatedClasses = db.getPackageInspector().
											findEntitiesInPackage(packageName);
				
		if (annotatedClasses == null || annotatedClasses.size() == 0) {
			// no @Entity-annotated classes found in package.
			throw new AnnotatedClassNotFoundInPackageException(packageName);
		}

		// still throws exception
		for (Class<?> currentClass : annotatedClasses) {
			registerEntity(currentClass);
		}
		
		setAutoPackageRegistration(false);
	}

	/**
	 * Makes physical name and data type bindings to entity and its fields then
	 * registers to the schema.
	 * 
	 * Postcondition: Disables auto-entity scanning.
	 * 
	 * @param entityClass
	 * @throws MappingSessionAlreadyStartedException
	 *             if session is already started.
	 */
	public static void registerEntity(Class<?> entityClass) {
		if (sessionStarted) {
			throw new MappingSessionAlreadyStartedException();
		}
		setAutoPackageRegistration(false);

		Entity e = new Entity(entityClass);

		Log.info("Registering entity %s.", e.getOriginalName());
		schema.addEntity(e);
	}

	protected static void registerSyntheticEntity(Entity e) {
		if (sessionStarted) {
			throw new MappingSessionAlreadyStartedException();
		}

		Log.info("Registering synthetic entity %s.", e.getOriginalName());
		schema.addEntity(e);
	}

	/**
	 * Returns {@link Entity} of given {@link Class}
	 * 
	 * @throws UnregisteredEntityException
	 *             if given <code>entityClass</code> is not found. Be careful
	 *             while using without a unregistered class. This throws
	 *             exception because getEntity() method assumed to be not
	 *             <code>null</code> in the rest of the project.
	 * @throws MappingSessionNotStartedException
	 * 
	 */
	public static Entity getEntity(Class<?> entityClass) {
		if (!sessionStarted)
			throw new MappingSessionNotStartedException();

		Entity e = schema.getBindedEntity(entityClass);

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

		return schema.getEntityByTableName(tableName);
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

		return schema.getEntityByClassName(className);
	}

	/**
	 * Starts the mapping session. According to {@link SchemaCreationPolicy}, in
	 * {@link MappingConfiguration}, it may drop and reconstruct all the tables
	 * from scratch.
	 * 
	 * <p>
	 * If no entities and packages are registered manually, it will create
	 * current project for @{@link Entity}-annotated classes and will throw
	 * {@link AnnotatedClassNotFoundInPackageException} if not found.
	 * </p>
	 * 
	 * <p>
	 * If you register an entity or package manually, automatic classpath
	 * scanning for entities will be disabled. This method should be called
	 * after registering all the @{@link Entity} annotated classes or containing
	 * packages if manual entity registration is used.
	 * </p>
	 * 
	 * <p>
	 * If the database is not registered before calling this method, again, an
	 * exception {@link NoDatabaseRegisteredException} will be thrown.
	 * </p>
	 * 
	 * <p>
	 * If the session is already started
	 * {@link MappingSessionAlreadyStartedException} will be thrown.
	 * </p>
	 * 
	 * @throws MappingSessionAlreadyStartedException
	 *             if already started
	 * @see MappingSession#registerEntity(Class)
	 * @see MappingSession#registerPackage(String)
	 * @see MappingSession#registerDatabase(Database)
	 */
	public static void start() {
		if (sessionStarted) {
			throw new MappingSessionAlreadyStartedException();
		}
		startNoCheck();
	}

	/**
	 * <p>
	 * If the mapping session is started, does not do anything, if it is not
	 * started, starts the session. It can be useful when <code>start()</code>
	 * requires to be called in multiple contexts in a single execution.
	 * </p>
	 * 
	 * @see MappingSession#start()
	 */
	public static void startSafe() {
		if (!sessionStarted) {
			start();
		}
	}

	/**
	 * <p>
	 * Does not throw {@link MappingSessionAlreadyStartedException} if the
	 * session already started. If used instead of <code>start()</code>, does
	 * not check whether session is already started or not and triggers physical
	 * bindings and execution of schema generator DDL queries.
	 * </p>
	 * 
	 * @see MappingSession#start() for a healthy bootstrap.
	 * 
	 * @throws UnmappedEntityException
	 *             if no physical name binding done before.
	 * @throws DuplicateTableNamesException
	 *             if binded physical name already exists in the schema.
	 */
	private static void startNoCheck() {
		preSessionStartHooks();

		sessionStarted = true; // mark the session as started.
		
		Log.info("Mapping session starting...");

		// BIND NAMES AND TYPES FOR FIELDS
		Log.info("Preparing to make physical bindings for entities.");
		for (Entity e : schema.getEntities()) {
			// schema.checkIdBinding(e);
			// TODO CRITICAL: Enable asap.

			// BIND TABLE NAME
			PhysicalNameAndTypeBindingEngine.makeBinding(e,
					configuration.getTableNamePolicy());
			if (e.getGeneratedName() == null || "".equals(e.getGeneratedName()))
				throw new UnmappedEntityException(e.getOriginalFullName());
			schema.checkConflictingEntities(e); // exception stops the check.

			// make generated name and type bindings to fields.
			if (e.getFields() != null)
				for (Field f : e.getFields()) {
					PhysicalNameAndTypeBindingEngine.makeBinding(e, f,
							configuration.getColumnNamePolicy(), typeMapper);
				}

			// check conflicting fields after bindings.
			schema.checkConflictingFields(e);
			Log.trace("No conflicting field names found on entity %s.",
					e.getOriginalName());
			
			EntityNameValidator.validateEntity(e);
		}

		// set custom SQL grammar provider binding
		SQLGrammarProvider p = db.getSQLGrammar();

		if (p != null) {
			Log.info("Custom SQL grammar found: " + p.getClass().getName());
			QueryType.setProvider(p);
			TableConstraintType.setProvider(p);
			IndexType.setProvider(p);
		}

		// CONSTRUCT DDL SCHEMA FINALLY
		constructSchema();

		System.gc(); // request garbage collection after session starts.
	}

	private static void preSessionStartHooks() {
		// scan current package and register @Entities if needed
		if (autoPackageRegistration) {
			
			if (db == null) {
				throw new NoDatabaseRegisteredException();
			}
			
			Log.info("Auto package registration enabled");
			registerPackage(db.getPackageInspector().
					getWorkingRootPackageName());
		}

		// Prepare synthetic (@ManyToMany) entities.
		prepareSyntheticEntities();
	}

	/**
	 * Scans the whole registered entities in the schema along with their fields
	 * and finds @{@link ManyToMany} annotated-fields then creates synthetic
	 * entities and *registers* them to the {@link MappingSession}.
	 */
	private static void prepareSyntheticEntities() {
		List<Entity> syntheticRegisterQueue = new LinkedList<Entity>();

		for (Entity e : schema.getEntities()) {
			for (Field f : e.getFields()) {
				if (f.isList() && f.isAnnotationPresent(ManyToMany.class)) {
					// if not already mapped on reverse side
					boolean exists = false;

					// already mapped?
					for (Entity cand : syntheticRegisterQueue) {
						Class<?>[] types = cand.getSyntheticTypes();
						if (types != null && types.length == 2) {
							// required to have 2 types by definition.
							Class<?> holderType = f.getRawField()
									.getDeclaringClass();
							Class<?> targetType = f.getClazz();

							exists = (holderType.equals(types[0]) && targetType
									.equals(types[1]))
									|| (holderType.equals(types[1]) && targetType
											.equals(types[0]));
						}
					}
					if (!exists) {
						Entity synthetic = new Entity(f);
						Log.trace(
								"Synthetic entity (%s, %s) created, registering.",
								synthetic.getSyntheticTypes()[0]
										.getSimpleName(), synthetic
										.getSyntheticTypes()[1].getSimpleName());
						syntheticRegisterQueue.add(synthetic);
					}
				}
			}
		}

		// process registration queue. registration inside loop causes
		// concurrent modification problems.
		for (Entity s : syntheticRegisterQueue) {
			registerSyntheticEntity(s);
		}
	}

	/**
	 * Returns synthetic entity managing {@link ManyToMany} relation.
	 * 
	 * @param holderType
	 *            a side of {@link ManyToMany} relation.
	 * @param targetType
	 *            a side of {@link ManyToMany} relation.
	 * @return <code>null</code> if not found.
	 */
	public static Entity getSyntheticEntity(Class<?> holderType,
			Class<?> targetType) {
		for (Entity cand : schema.getEntities()) {
			if (cand.isSynthetic()) {
				Class<?>[] types = cand.getSyntheticTypes();
				if (types != null && types.length == 2) {
					// required to have 2 types by definition.
					if ((holderType.equals(types[0]) && targetType
							.equals(types[1]))
							|| (holderType.equals(types[1]) && targetType
									.equals(types[0]))) {
						return cand;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Prepares DDL queries to create existing schema from scratch.
	 * 
	 * TODO return them as a list to execute somehow.
	 */
	private static void constructSchema() {
		Queue<Query> constructionQueries = new LinkedList<Query>();

		SchemaCreationPolicy policy = configuration.getCreationPolicy();

		Log.info("Schema creation policy is %s.", policy.toString());

		if (policy.equals(SchemaCreationPolicy.CREATE)
				|| policy.equals(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS)) {

			EntityDependencyGraph serialSchedule = new EntityDependencyGraph(
					schema.getEntities());

			// Drop tables
			for (Entity e : serialSchedule.getDestroySchedule()) {
				if (policy.equals(SchemaCreationPolicy.CREATE)) {
					// DROP TABLE IF EXISTS
					Query dT = DDLQueryGenerator.dropTableQuery(e);
					constructionQueries.offer(dT);
				}
			}

			// Create tables
			for (Entity e : serialSchedule.getConstructSchedule()) {
				// CREATE TABLE
				Query cT = DDLQueryGenerator.createTableQuery(e,
						policy.equals(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS));
				constructionQueries.offer(cT);

				// CREATE INDEXES
				List<Field> compositeIndexFields = new ArrayList<Field>(2);
				List<Field> singleIndexFields = new ArrayList<Field>(2);
				for (Field f : e.getFields()) {
					if (f.getIndex() != null) {
						if (f.isPrimaryKey() && !f.isAutoIncrement())
							compositeIndexFields.add(f);
						if (!f.isPrimaryKey()) {
							singleIndexFields.add(f);
						}
					}
				}

				// Process composite index (primary keys).
				if (policy.equals(SchemaCreationPolicy.CREATE)) {
					// DROP INDEX first
					Query dCI = DDLQueryGenerator.dropCompositeIndexQuery(e);
					if (dCI != null)
						constructionQueries.offer(dCI);
				}
				// CREATE INDEX compositely, then
				Query cCI = DDLQueryGenerator.createCompositeIndexQuery(e,
						compositeIndexFields,
						policy.equals(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS));
				if (cCI != null)
					constructionQueries.offer(cCI);

				// Create single indexes
				for (Field f : singleIndexFields) {
					if (policy.equals(SchemaCreationPolicy.CREATE)) {
						// DROP INDEX first
						Query dI = DDLQueryGenerator.dropIndexQuery(e, f);
						constructionQueries.offer(dI);
					}
					// CREATE INDEX then
					Query cI = DDLQueryGenerator.createIndexQuery(e, f,
							policy.equals(SchemaCreationPolicy.CREATE_IF_NOT_EXISTS));
					constructionQueries.offer(cI);
				}
			}

			Log.info("Executing DDL construction queries.");
			for (Query q : constructionQueries) {
				getExecuter().executeOnly(q);
			}
			Log.info("DDL constructed successfully.");
		}
	}

	/**
	 * Enables/Disables auto package registration system
	 * 
	 * @param enabled
	 *            status of the auto registration
	 */
	public static void setAutoPackageRegistration(boolean enabled) {
		autoPackageRegistration = enabled;
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