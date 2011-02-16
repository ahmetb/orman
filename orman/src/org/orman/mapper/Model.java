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
	}
	
	private Query prepareInsertQuery() {
		QueryBuilder qb = QueryBuilder.insert();
		qb.from(getEntity().getGeneratedName()); // set table name
		
		// bind fields and their values
		for(Field f : getEntity().getFields()){
			qb.set(f.getGeneratedName(), getEntityField(f, getEntity(), this));
		}
		
		return qb.getQuery();
	}

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
			}
		} else { // not public field, invoke method!
			try {
				return setter.invoke(instance, value);
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and accessible.
			}
		}
		return null;
	}


	public void update(){
		
	}
}
