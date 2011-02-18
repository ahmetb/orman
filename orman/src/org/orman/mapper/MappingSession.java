package org.orman.mapper;

import java.util.LinkedList;
import java.util.Queue;

import org.orman.datasource.DataTypeMapper;
import org.orman.mapper.exception.MappingSessionAlreadyStartedException;
import org.orman.mapper.exception.UnregisteredEntityException;
import org.orman.mysql.DataTypeMapperImpl;
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
	private static DataTypeMapper typeMapper;
	private static boolean sessionStarted = false;

	static {
		scheme = new PersistenceSchemeMapper();
		configuration = new MappingConfiguration();
		typeMapper = new DataTypeMapperImpl(); // TODO experimental
		// implementation, remove soon.
	}

	/**
	 * Makes physical name and data type bindings to entity and its fields then
	 * registers to the scheme.
	 * 
	 * @param entityClass
	 * @return scheme properties-binded entity
	 */
	public static Entity registerEntity(Class<?> entityClass) {
		Entity e = new Entity(entityClass);

		scheme.checkIdBinding(e);

		for (Field f : e.getFields()) {
			PhysicalNameAndTypeBindingEngine.makeBinding(f, configuration
					.getColumnNamePolicy(), typeMapper);
		}

		scheme.checkConflictingFields(e);

		PhysicalNameAndTypeBindingEngine.makeBinding(e, configuration
				.getTableNamePolicy());

		scheme.addEntity(e);

		return e;
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
		Entity e = scheme.getBindedEntity(entityClass);

		if (e == null)
			throw new UnregisteredEntityException(entityClass.getName());

		return e;
	}

	/**
	 * Returns {@link Entity} of given physical table name.
	 * 
	 * @param tableName
	 *            Caution: case-sensitive
	 * @return
	 */
	public static Entity getEntityByTableName(String tableName) {
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

		sessionStarted = true;
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

		if (configuration.getCreationPolicy() == SchemeCreationPolicy.CREATE) {
			for (Entity e : scheme.getEntities()) {
				Query dT = DDLQueryGenerator.dropTableQuery(e);
				constructionQueries.offer(dT);

				Query cT = DDLQueryGenerator.createTableQuery(e);
				constructionQueries.offer(cT);

				for (Field f : e.getFields()) {
					if (f.getIndex() != null) {
						Query cI = DDLQueryGenerator.createIndexQuery(e, f);
						constructionQueries.offer(cI);
					}
				}
			}

			System.out.println(constructionQueries); // TODO instead, execute.
		}
	}

	public static MappingConfiguration getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(MappingConfiguration configuration) {
		MappingSession.configuration = configuration;
	}

	public static void setTypeMapper(DataTypeMapper typeMapper) {
		MappingSession.typeMapper = typeMapper;
	}
}