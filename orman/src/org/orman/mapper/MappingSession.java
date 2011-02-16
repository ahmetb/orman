package org.orman.mapper;

import java.util.LinkedList;
import java.util.Queue;

import org.orman.mapper.exception.MappingSessionAlreadyStartedException;
import org.orman.mapper.exception.UnregisteredEntityException;
import org.orman.mysql.DataTypeMapperImpl;
import org.orman.sql.Query;

/**
 * Mapping session for static system-wide scope.
 * It is statically initialized and entities should
 * register themselves in order to get served.
 * 
 * Provides query execution and transaction managers so that
 * it is a façade class.
 * 
 * @author alp
 *
 */
public class MappingSession {
	private static PersistenceSchemeMapper scheme;
	private static MappingConfiguration configuration;
	private static DataTypeMapper typeMapper;
	private static boolean sessionStarted = false;

	static{
		scheme = new PersistenceSchemeMapper();
		configuration = new MappingConfiguration();
		typeMapper = new DataTypeMapperImpl(); // TODO experimental implementation, remove soon.
	}
	
	/**
	 * Makes name and type bindings to entity and its fields
	 * then registers to the scheme.
	 * 
	 * @param entityClass
	 * @return scheme props-binded entity  
	 */
	public static Entity registerEntity(Class<?> entityClass){
		Entity e = new Entity(entityClass);

		scheme.checkIdBinding(e);
		
		for(Field f : e.getFields()){
			PhysicalNameAndTypeBindingEngine.makeBinding(f, configuration.getColumnNamePolicy(), typeMapper);
		}
		
		scheme.checkConflictingFields(e);
		

		PhysicalNameAndTypeBindingEngine.makeBinding(e, configuration.getTableNamePolicy());
		
		scheme.addEntity(e);
		
		return e;
	}
	
	public static Entity getEntity(Class<?> entityClass){
		Entity e = scheme.getBindedEntity(entityClass);
		
		if (e == null)
			throw new UnregisteredEntityException(entityClass.getName());
		
		return e;
	}
	
	public static Entity getEntity(String tableName){
		return scheme.getEntityByTableName(tableName);
	}
	
	public static void start(){
		if (sessionStarted)
			throw new MappingSessionAlreadyStartedException();

		sessionStarted = true;
		constructScheme();
	}

	private static void constructScheme() {
		Queue<Query> constructionQueries = new LinkedList<Query>();
		
		if (configuration.getCreationPolicy() == SchemeCreationPolicy.CREATE){
			for(Entity e: scheme.getEntities()){
				Query dT = DDLQueryGenerator.dropTableQuery(e);
				constructionQueries.offer(dT);
				
				Query cT = DDLQueryGenerator.createTableQuery(e);
				constructionQueries.offer(cT);
				
				for(Field f : e.getFields()){
					if (f.getIndex() != null){
						Query cI = DDLQueryGenerator.createIndexQuery(e, f);
						constructionQueries.offer(cI);
					}
				}
			}
			
			System.out.println(constructionQueries); // TODO instead, execute.
		}
	}
	
}