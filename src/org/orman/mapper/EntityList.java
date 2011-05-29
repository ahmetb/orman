package org.orman.mapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.orman.exception.FeatureNotImplementedException;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.exception.FieldNotFoundException;
import org.orman.sql.Query;
import org.orman.util.logging.Log;

/**
 * This is a {@link List} implementation which is designed for managing @
 * {@link ManyToOne} fields. It will manage ManyToOne relationship therefore it
 * can keep entity list (which is on @{@link ManyToOne} holding class) and the
 * entity (holds @{@link OneToMany} field) synchronized, provides consistency of
 * data.
 * 
 * <p>
 * It also manages LAZY LOADING mechanism for retrieved entities. {@link LoadingPolicy}
 * </p>
 * 
 * <p>
 * <u>CAUTION:</u> It is compulsory if you are managing a @{@link ManyToOne}
 * field.
 * </p>
 * 
 * @param <D> the entity class of holder type.
 * @param <E> the entity class of target type.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 */

public class EntityList<D, E extends Model<E>> implements List<E> {
	private boolean lazyLoadingEnabled = false;
	private boolean lazyLoaded = false;
	
	private D holderInstance;
	private Class<D> holderType;
	private Class<E> targetType;
	private List<E> elements;
	private Entity holderEntity;
	private Entity targetEntity;

	public EntityList(Class<D> holderType, Class<E> targetType, D holderInstance){
		this.holderInstance = holderInstance;
		this.holderType = holderType;
		this.targetType = targetType;
		this.lazyLoadingEnabled = false;
		
		holderEntity = MappingSession.getEntity(holderType);
		targetEntity = MappingSession.getEntity(targetType);
	}
	
	public EntityList(Class<D> holderType, Class<E> targetType, D holderInstance, boolean isLazyLoading){
		this(holderType, targetType, holderInstance);
		this.lazyLoadingEnabled = isLazyLoading;
	}
	
	public EntityList(Class<D> holderType, Class<E> targetType, D holderInstance, List<E> existingResultList){
		this(holderType, targetType, holderInstance);
		this.elements = existingResultList;
	}
	
	private void lazyLoadIfNeeded() {
		// if lazy loading exists and not executed OR usual initialization
		if ((lazyLoadingEnabled && !lazyLoaded) || elements == null){
			if (lazyLoadingEnabled) lazyLoaded = true;
			
			refreshList();
		}
	}
	
	/**
	 * Refreshes entity list from the database without any conditions
	 * satisfied.
	 */
	public void refreshList() {
		Query q = ModelQuery
		.select()
		.from(targetType)
		.where(C.eq(targetType,
				getTargetField(holderEntity, targetEntity).getGeneratedName(),
				holderInstance)).getQuery();

		elements = Model.fetchQuery(q, targetType);
		Log.trace("Fetched %d target entities to EntityList.", elements.size());
	}

	public synchronized boolean add(E e) {
		if (e == null) return false;
		lazyLoadIfNeeded();
		
		Entity holderEntity = MappingSession.getEntity(holderType);
		Entity targetEntity = MappingSession.getEntity(targetType);
		
		// find @OneToMany class on holderType
		// then get on() from it.
		Field targetField = getTargetField(holderEntity, targetEntity);


		e.setEntityField(targetField, targetEntity, holderInstance);
		e.update();
		
		elements.add(e); // add to list.
		
		return true;
	}

	private Field getTargetField(Entity holderEntity, Entity targetEntity) {
		// TODO this class assumes there exists only OneToMany in one class.
		for(Field i : holderEntity.getFields()){
			OneToMany ann = i.getAnnotation(OneToMany.class);
			if (ann != null){
				String targetFieldName = ann.on();
				Field targetField = targetEntity.getFieldByName(targetFieldName);
				
				if (targetField == null) throw new FieldNotFoundException(holderEntity.getOriginalFullName(), targetFieldName);
				return targetField;
			}
		}
		return null; // unreachabdle
	}

	public void add(int index, E element) {
		add(element);
	}

	public boolean addAll(Collection<? extends E> c) {
		for(E e : c) {
			boolean result = add(e);
			
			if(!result){
				// terminate insertions immediately
				// not transactional.
				return false;
			}
		}
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return addAll(c);
	}

	/**
	 * Deletes all items connected to holder entity instance
	 * and also removes them from entity list. 
	 */
	public void clear() {
		lazyLoadIfNeeded();
		
		for(E e : elements){
			e.delete();
		}
		elements.clear(); // remove from list.
	}

	public boolean contains(Object o) {
		lazyLoadIfNeeded();
		
		return elements.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		lazyLoadIfNeeded();
		
		return elements.containsAll(c);
	}

	public E get(int index) {
		lazyLoadIfNeeded();
		
		return elements.get(index);
	}

	public int indexOf(Object o) {
		lazyLoadIfNeeded();
		
		return elements.indexOf(o);
	}

	public boolean isEmpty() {
		lazyLoadIfNeeded();
		return elements.isEmpty();
	}

	public Iterator<E> iterator() {
		lazyLoadIfNeeded();
		return elements.iterator();
	}

	public int lastIndexOf(Object o) {
		lazyLoadIfNeeded();
		return elements.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		lazyLoadIfNeeded();
		return elements.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		lazyLoadIfNeeded();
		return elements.listIterator(index);
	}

	public boolean remove(Object o) {
		lazyLoadIfNeeded();
		
		@SuppressWarnings("unchecked")
		E e = (E) o;
		
		if (e == null) return false;
		else {
			e.delete();
			return elements.remove(o);
		}
	}

	public E remove(int index) {
		lazyLoadIfNeeded();
		E e = elements.remove(index);
		
		if (e == null){ return null; }
		else {
			e.delete();
			return e;
		}
	}

	public boolean removeAll(Collection<?> c) {
		for(Object o : c){
			boolean result = remove(o);
			if (!result){ // early termination
				return false;
			}
		}
		return true;
	}

	public boolean retainAll(Collection<?> c) { // bunelan?
		throw new FeatureNotImplementedException("This method is not implemented on EntityList.");
	}

	public E set(int index, E element) {
		throw new FeatureNotImplementedException("This method is not supported on EntityList.");
	}

	public int size() {
		lazyLoadIfNeeded();
		return elements.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		throw new FeatureNotImplementedException("This method is not implemented on EntityList.");
	}

	public Object[] toArray() {
		lazyLoadIfNeeded();
		return elements.toArray();
	}

	public <T> T[] toArray(T[] a) {
		lazyLoadIfNeeded();
		return elements.toArray(a);
	}

	public String toString() {
		lazyLoadIfNeeded();
		return (elements == null) ? null : elements.toString();
	}
}
