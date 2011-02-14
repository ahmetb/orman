package org.orman.mapper;

import org.orman.mysql.DataTypeMapperImpl;

/**
 * Mapping session for system-wide scope.
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
	private PersistenceSchemeMapper scheme;
	private MappingConfiguration configuration;
	private DataTypeMapper typeMapper;
	
	public MappingSession(){
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
	public Entity registerEntity(Class<?> entityClass){
		Entity e = new Entity(entityClass);

		for(Field f : e.getFields()){
			PhysicalNameAndTypeBindingEngine.makeBinding(f, configuration.getColumnNamePolicy(), typeMapper);
		}
		
		scheme.checkConflictingFields(e);

		PhysicalNameAndTypeBindingEngine.makeBinding(e, configuration.getTableNamePolicy());
		
		scheme.addEntity(e);
		
		return e;
	}
	
	public Entity getEntity(Class<?> entityClass){
		return scheme.getBindedEntity(entityClass);
	}
	
	public Entity getEntity(String tableName){
		return scheme.getEntityByTableName(tableName);
	}
	
}