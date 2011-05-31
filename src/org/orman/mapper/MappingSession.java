package org.orman.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.orman.datasource.DataTypeMapper;
import org.orman.datasource.Database;
import org.orman.datasource.QueryExecutionContainer;
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
 * @author 0ffffffffh
 */
public class MappingSession {
	private static PersistenceSchemeMapper scheme;
	private static MappingConfiguration configuration;

	private static Database db;
	private static DataTypeMapper typeMapper;
	private static QueryExecutionContainer executer;

	private static boolean sessionStarted = false;
	private static boolean autoPackageRegistration = true;

	static {
		scheme = new PersistenceSchemeMapper();
		configuration = new MappingConfiguration();
		// implementation, remove soon.
	}

	public static void registerDatabase(Database database) {
		
		if (autoPackageRegistration) {
			Log.info("Auto package registration enabled");
			registerPackage(PackageEntityInspector.getWorkingRootPackageName());
		}
		
		db = database;
		typeMapper = database.getTypeMapper();
		executer = database.getExecuter();
	}
	

	/**
	 * Finds and registers @{@link Entity}-annotated classes in given package,
	 * recursively. See also <code>registerEntity(Class<?>)</code> method for
	 * more.
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
		List<Class<?>> annotatedClasses = PackageEntityInspector
				.findEntitiesInPackage(packageName);

		if (annotatedClasses == null || annotatedClasses.size() == 0) {
			// no @Entity-annotated classes found in package.
			AnnotatedClassNotFoundInPackageException ex = new AnnotatedClassNotFoundInPackageException(
					packageName);
			Log.error(ex.getMessage());
			throw ex;
		}

		// still throws exception
		for (Class<?> currentClass : annotatedClasses) {
			registerEntity(currentClass);
		}
	}

	/**
	 * Makes physical name and data type bindings to entity and its fields then
	 * registers to the scheme.
	 * 
	 * @param entityClass
	 * @throws MappingSessionAlreadyStartedException
	 *             if session is already started.
	 */
	public static void registerEntity(Class<?> entityClass) {
		if (sessionStarted) {
			MappingSessionAlreadyStartedException ex = new MappingSessionAlreadyStartedException();
			Log.error(ex.getMessage());
			throw ex;
		}

		Entity e = new Entity(entityClass);

		Log.info("Registering entity %s.", e.getOriginalName());
		scheme.addEntity(e);
	}

	protected static void registerSyntheticEntity(Entity e) {
		if (sessionStarted) {
			MappingSessionAlreadyStartedException ex = new MappingSessionAlreadyStartedException();
			Log.error(ex.getMessage());
			throw ex;
		}

		Log.info("Registering synthetic entity %s --> %s.", e.getOriginalName(), e.getGeneratedName());
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
	 * @throws MappingSessionNotStartedException 
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
	 * <p>
	 * If the session is already started
	 * {@link MappingSessionAlreadyStartedException} will be thrown.
	 * </p>
	 * 
	 */
	public static void start() {
		if (sessionStarted) {
			MappingSessionAlreadyStartedException e = new MappingSessionAlreadyStartedException();
			Log.error(e.getMessage());
			throw e;
		}
		startNoCheck();
	}

	/**
	 * <p>
	 * Does not throw {@link MappingSessionAlreadyStartedException} if the
	 * session already started. If used instead of <code>start()</code>, does
	 * not check whether session is already started or not and triggers physical
	 * bindings and execution of scheme generator DDL queries.
	 * </p>
	 * 
	 * See <code>start()</code> for a healthy bootstrap.
	 * 
	 * @throws UnmappedEntityException if no physical name binding
	 * done before.
	 * @throws DuplicateTableNamesException if binded physical name already
	 * exists in the scheme.
	 */
	private static void startNoCheck() {
		preSessionStartHooks();

		sessionStarted = true; // mark the session as started.

		Log.info("Mapping session starting...");

		// BIND NAMES AND TYPES FOR FIELDS
		Log.info("Preparing to make physical bindings for entities.");
		for (Entity e : scheme.getEntities()) {
			// scheme.checkIdBinding(e);
			// TODO CRITICAL: Enable asap.

			// BIND TABLE NAME
			PhysicalNameAndTypeBindingEngine.makeBinding(e,
					configuration.getTableNamePolicy());
			if (e.getGeneratedName() == null || "".equals(e.getGeneratedName()))
				throw new UnmappedEntityException(e.getOriginalFullName());
			scheme.checkConflictingEntities(e); // exception stops the check.

			// make generated name and type bindings to fields.
			if(e.getFields() != null)
			for (Field f : e.getFields()) {
				PhysicalNameAndTypeBindingEngine.makeBinding(e, f,
						configuration.getColumnNamePolicy(), typeMapper);
			}

			// check conflicting fields after bindings.
			scheme.checkConflictingFields(e);
			Log.info("No conflicting field names found on entity %s.",
					e.getOriginalName());
		}

		if (db == null) {
			NoDatabaseRegisteredException e = new NoDatabaseRegisteredException();
			Log.error(e.getMessage());
			throw e;
		}

		// set custom SQL grammar provider binding
		SQLGrammarProvider p = db.getSQLGrammar();

		if (p != null) {
			Log.info("Custom SQL grammar found: " + p.getClass().getName());
			QueryType.setProvider(p);
			TableConstraintType.setProvider(p);
			IndexType.setProvider(p);
		}

		// CONSTRUCT DDL SCHEME FINALLY
		constructScheme();
	}

	private static void preSessionStartHooks() {
		// Prepare synthetic (@ManyToMany) entities.
		prepareSyntheticEntities();
	}

	/**
	 * Scans the whole registered entities in the scheme along with their fields
	 * and finds @{@link ManyToMany} annotated-fields then creates synthetic
	 * entities and *registers* them to the {@link MappingSession}.
	 */
	private static void prepareSyntheticEntities() {
		List<Entity> syntheticRegisterQueue = new LinkedList<Entity>();
		
		for (Entity e : scheme.getEntities()) {
			for (Field f : e.getFields()) {
				if (f.isList() && f.isAnnotationPresent(ManyToMany.class)) {
					// if not already mapped on reverse side
					boolean exists = false;
					
					// already mapped?
					for(Entity cand : syntheticRegisterQueue){
						Class<?>[] types = cand.getSyntheticTypes(); 
						if (types != null && types.length == 2){
							// required to have 2 types by definition.
							Class<?> holderType = f.getRawField().getDeclaringClass();
							Class<?> targetType = f.getClazz();
							
							exists = (holderType.equals(types[0]) && targetType
									.equals(types[1]))
									|| (holderType.equals(types[1]) && targetType
											.equals(types[0]));
						}
					}
					if (!exists){
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
		for(Entity s : syntheticRegisterQueue){
			registerSyntheticEntity(s);
		}
	}
	
	/**
	 * Returns synthetic entity managing {@link ManyToMany} relation.
	 * 
	 * @param holderType a side of {@link ManyToMany} relation.
	 * @param targetType a side of {@link ManyToMany} relation.
	 * @return <code>null</code> if not found.
	 */
	public static Entity getSyntheticEntity(Class<?> holderType, Class<?> targetType) {
		for (Entity cand : scheme.getEntities()) {
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
	 * Prepares DDL queries to create existing scheme from scrats.
	 * 
	 * TODO return them as a list to execute somehow.
	 */
	private static void constructScheme() {
		Queue<Query> constructionQueries = new LinkedList<Query>();

		SchemeCreationPolicy policy = configuration.getCreationPolicy();

		Log.info("Scheme creation policy is %s.", policy.toString());

		if (policy.equals(SchemeCreationPolicy.CREATE)
				|| policy.equals(SchemeCreationPolicy.UPDATE)) {

			// TODO Discuss: Order of drop of existing tables. Current policy,
			// tbls with most FK first.
			Collections.sort(scheme.getEntities(), new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) { // ORDER BY fk_count
															// DESC
					return new Integer(o2.getForeignKeyCount()).compareTo(o1
							.getForeignKeyCount());
				}
			});

			for (Entity e : scheme.getEntities()) {
				if (policy.equals(SchemeCreationPolicy.CREATE)) {
					// DROP TABLE IF EXISTS
					Query dT = DDLQueryGenerator.dropTableQuery(e);
					constructionQueries.offer(dT);
				}
			}

			// TODO Discuss: Order of creation of tables. Current policy,
			// entities with lesser FK first.
			Collections.sort(scheme.getEntities(), new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) { // ORDER BY fk_count
															// ASC
					return new Integer(o1.getForeignKeyCount()).compareTo(o2
							.getForeignKeyCount());
				}
			});

			for (Entity e : scheme.getEntities()) {
				// CREATE TABLE
				Query cT = DDLQueryGenerator.createTableQuery(e,
						policy.equals(SchemeCreationPolicy.UPDATE));
				constructionQueries.offer(cT);

				// CREATE INDEXES
				List<Field> compositeIndexFields = new ArrayList<Field>(2);
				List<Field> singleIndexFields = new ArrayList<Field>(2);
				// TODO implement composite indexes.
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
				if (policy.equals(SchemeCreationPolicy.CREATE)) {
					// DROP INDEX first
					Query dCI = DDLQueryGenerator.dropCompositeIndexQuery(e);
					if (dCI != null)
						constructionQueries.offer(dCI);
				}
				// CREATE INDEX compositely, then
				Query cCI = DDLQueryGenerator.createCompositeIndexQuery(e,
						compositeIndexFields,
						policy.equals(SchemeCreationPolicy.UPDATE));
				if (cCI != null)
					constructionQueries.offer(cCI);

				// Create single indexes
				for (Field f : singleIndexFields) {
					if (policy.equals(SchemeCreationPolicy.CREATE)) {
						// DROP INDEX first
						Query dI = DDLQueryGenerator.dropIndexQuery(e, f);
						constructionQueries.offer(dI);
					}
					// CREATE INDEX then
					Query cI = DDLQueryGenerator.createIndexQuery(e, f,
							policy.equals(SchemeCreationPolicy.UPDATE));
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
	 * @param enabled status of the auto registration
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