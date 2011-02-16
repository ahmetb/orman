package org.orman.mapper;

import java.lang.reflect.Method;

import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;

/**
 * Parent class for Entities providing basic persistency methods
 * for POJOs.
 * 
 * @author alp
 * 
 */
public class Model<E> {
	
	private Class<E> clazz;
	private Entity __mappedEntity;
	
	@SuppressWarnings("unchecked")
	public Model(){
		this.clazz = (Class<E>) this.getClass();
	}
	
	private Entity getEntity(){
		if (__mappedEntity == null){
			__mappedEntity = MappingSession.getEntity(this.clazz);
		}
		return __mappedEntity;
	}
	
	public void insert(){
		Query q = prepareInsertQuery();
		System.out.println(q.toString());
		
		/* TODO if DEFER_TO_DBMS, get last_insert_id 
		 * and attach to the instance here!  
		 */
	}
	
	private Query prepareInsertQuery() {
		QueryBuilder qb = QueryBuilder.insert();
		qb.from(getEntity().getGeneratedName()); // set table name
		
		// bind fields and their values
		for(Field f : getEntity().getFields()){
			boolean useField = true;
			
			if (f.isId()){
				IdGenerationPolicy policy = MappingSession.getConfiguration().getIdGenerationPolicy();
				
				if (policy == IdGenerationPolicy.ORMAN_ID_GENERATOR)
					/* bind generated id to the transient instance */
					setEntityField(f, getEntity(), this, NativeIdGenerator.generate(f, this));
				if (policy == IdGenerationPolicy.DEFER_TO_DBMS)
					useField = false;
			}
			
			if (useField) // use field in query
				qb.set(f.getGeneratedName(), getEntityField(f, getEntity(), this));
		}
		return qb.getQuery();
	}
	
	/**
	 * Needs to be casted field.getClazz().
	 */
	private Object getEntityField(Field field, Entity of, Model<E> instance) {
		Method getter = field.getGetterMethod();
		
		if(getter == null){ // field is already public
			try{
				return of.getClazz().getDeclaredField(field.getOriginalName()).get(instance);
			} catch(Exception e){
				// TODO caution: assuming field certainly exists and accessible.
			}
		} else { // not public field, invoke method!
			try {
				return getter.invoke(instance);
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and accessible.
			}
		}
		return null;
	}
	
	private Object setEntityField(Field field, Entity of, Model<E> instance, Object value) {
		Method setter = field.getSetterMethod();
		
		if(setter == null){ // field is already public 
			try{
				of.getClazz().getDeclaredField(field.getOriginalName()).set(instance, value);
			} catch(Exception e){
				// TODO caution: assuming field certainly exists and accessible.
				e.printStackTrace();
			}
		} else { // not public field, invoke method!
			try {
				return setter.invoke(instance, value);
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and accessible.
				e.printStackTrace();
			}
		}
		return null;
	}
}
