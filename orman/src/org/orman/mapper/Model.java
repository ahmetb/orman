package org.orman.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.orman.sql.C;
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
	private static final int DEFAULT_TRANSIENT_HASHCODE = -1;
	
	private Class<E> clazz;
	private Entity __mappedEntity;
	
	private int __persistencyHash = DEFAULT_TRANSIENT_HASHCODE;
	private Object __persistencyId;
	private int[] __persistencyFieldHashes;
	
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
	
	public boolean isPersistent(){
		return (this.hashCode() == __persistencyHash)
				&& __persistencyHash != DEFAULT_TRANSIENT_HASHCODE;
	}
	
	public void insert(){
		// TODO check: is already persistent?
		
		Query q = prepareInsertQuery();
		System.out.println(q.toString()); // TODO execute instead.
		
		/* TODO if DEFER_TO_DBMS, get last_insert_id 
		 * and attach to the instance here!  
		 */
		
		makePersistent();
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
	
	public void update(){
		// TODO check: is persistent to update?
		
		Query q = prepareUpdateQuery();
		System.out.println(q);  // TODO execute instead.
		
		makePersistent();
	}
	
	private Query prepareUpdateQuery(){
		List<Field> updatedFields = new ArrayList<Field>();
		List<Field> fields = getEntity().getFields();
		
		for(int i = 0; i < __persistencyFieldHashes.length; i++){
			Object o = getEntityField(fields.get(i),
					getEntity(), this);
			if (((o == null) ? DEFAULT_TRANSIENT_HASHCODE
					: o.hashCode()) != __persistencyFieldHashes[i])
				updatedFields.add(fields.get(i));
		}
		
		if(!updatedFields.isEmpty()){
			QueryBuilder qb = QueryBuilder.update().from(getEntity().getGeneratedName());
			for(Field f: updatedFields){
				qb.set(f.getGeneratedName(), getEntityField(f, getEntity(), this));
			}
			qb.where(C.eq(getEntity().getIdField().getGeneratedName(), __persistencyId));
			return qb.getQuery();
		} else return null;
	}

	
	public void delete(){
		// TODO check: is persistent to delete?
		Query q = prepareDeleteQuery();
		System.out.println(q);  // TODO execute instead.
		
		makeTransient();
	}
	
	private Query prepareDeleteQuery() {
		return QueryBuilder.delete()
		.from(getEntity().getGeneratedName())
		.where(
				C.eq(getEntity().getIdField().getGeneratedName(), __persistencyId)
				)
		.getQuery();
				
	}

	private Object getEntityId(){
		Field idField = getEntity().getIdField();
		return getEntityField(idField, getEntity(), this);
	}
	
	private void makePersistent(){
		List<Field> fields = getEntity().getFields();
		__persistencyFieldHashes = new int[fields.size()];
		
		for(int i = 0; i < __persistencyFieldHashes.length; i++){
			Object o = getEntityField(fields.get(i), getEntity(), this);
			__persistencyFieldHashes[i] = (o == null) ? DEFAULT_TRANSIENT_HASHCODE
					: o.hashCode();
		}
		__persistencyId = getEntityId();
		__persistencyHash = this.hashCode();
	}
	
	
	private void makeTransient() {
		List<Field> fields = getEntity().getFields();
		__persistencyFieldHashes = new int[fields.size()];
		__persistencyId = DEFAULT_TRANSIENT_HASHCODE;
		__persistencyHash = DEFAULT_TRANSIENT_HASHCODE;
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

	// TODO idea: hashcode implementation for check for breaking up persistent instance?
	/**
	 * Hashcode implementation for persistent (non-transient) fields.
	 */
	@Override
	public int hashCode() {
		int hash = 1;
		for(Field f : getEntity().getFields()){
			Object o = getEntityField(f, getEntity(), this);
			hash += (o==null ? 1 : o.hashCode()) * 7; // magic.
		}
		
		// prevent coincidently correspontransiency flag
		return hash == DEFAULT_TRANSIENT_HASHCODE ? hash | 0x9123 : hash;

	}
}