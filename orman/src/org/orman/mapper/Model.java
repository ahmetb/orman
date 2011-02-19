package org.orman.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.orman.mapper.annotation.Id;
import org.orman.mapper.exception.NotNullableFieldException;
import org.orman.mapper.exception.UnableToPersistDetachedEntityException;
import org.orman.mapper.exception.UnableToSaveDetachedInstanceAsFieldException;
import org.orman.sql.C;
import org.orman.sql.Query;
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;

/**
 * Parent class for {@link org.orman.mapper.annotation.Entity}-annoted classes
 * providing basic persistency methods for POJOs such as insert(), update(),
 * delete(). Holds the persistency status i.e. the current instance is detached
 * or persistent etc. Provides methods to get-set values of fields in this
 * domain class..
 * 
 * @author alp
 */
public class Model<E> {
	private static final int DEFAULT_TRANSIENT_HASHCODE = -1;

	private Class<E> clazz;
	private Entity __mappedEntity;

	private int __persistencyHash = DEFAULT_TRANSIENT_HASHCODE;
	private Object __persistencyId;
	private int[] __persistencyFieldHashes;

	@SuppressWarnings("unchecked")
	public Model() {
		this.clazz = (Class<E>) this.getClass();
	}

	private Entity getEntity() {
		if (__mappedEntity == null) {
			__mappedEntity = MappingSession.getEntity(this.clazz);
		}
		return __mappedEntity;
	}

	/**
	 * Can be used to find that the instance is exactly
	 * as its state when saved in terms of persistency.
	 * 
	 * Detached instances are not saved at all or changed
	 * after saving or fetching from database.
	 * 
	 * @return false if object is changed or not saved at all,
	 * true if the ob
	 */
	public boolean isPersistent() {
		return (this.hashCode() == __persistencyHash)
				&& __persistencyHash != DEFAULT_TRANSIENT_HASHCODE;
	}

	/**
	 * Inserts the instance to the database as row and then binds generated id
	 * if IdGenerationPolicy is DEFER_TO_DBMS.
	 * 
	 * Postcondition: Instance is persistent.
	 */
	public void insert() {
		// TODO discuss: persistency check?

		Query q = prepareInsertQuery();

		MappingSession.getExecuter().executeOnly(q);

		// Bind last insert id if IdGenerationPolicy is DEFER_TO_DBMS
		if (MappingSession.getConfiguration().getIdGenerationPolicy().equals(
				IdGenerationPolicy.DEFER_TO_DBMS)) {
			Field idField = getEntity().getIdField();
			setEntityField(idField, getEntity(), this, MappingSession
					.getExecuter().getLastInsertId(idField.getClazz()));
		}

		makePersistent();
	}

	private Query prepareInsertQuery() {
		QueryBuilder qb = QueryBuilder.insert();
		qb.from(getEntity().getGeneratedName()); // set table name

		// bind fields and their values
		for (Field f : getEntity().getFields()) {
			boolean useField = true;

			// make id value binding
			if (f.isId()) {
				IdGenerationPolicy policy = MappingSession.getConfiguration()
						.getIdGenerationPolicy();

				if (policy == IdGenerationPolicy.ORMAN_ID_GENERATOR)
					/* bind generated id to the transient instance */
					setEntityField(f, getEntity(), this, NativeIdGenerator
							.generate(f, this));
				if (policy == IdGenerationPolicy.DEFER_TO_DBMS)
					useField = false;
			}

			if (useField) { // use field in query
				Object fieldVal = getEntityField(f, getEntity(), this);

				if (!f.isNullable() && fieldVal == null) // prevent saving null
															// on @NotNull
					throw new NotNullableFieldException(getEntity()
							.getOriginalFullName(), f.getOriginalName());

				if (fieldVal != null && fieldVal instanceof Model<?>) {
					Model<?> instance = (Model<?>) fieldVal;

					fieldVal = fieldValueSerializer(fieldVal);

					if (!instance.isPersistent()) {
						throw new UnableToSaveDetachedInstanceAsFieldException(f
								.getOriginalName(), instance.getClass()
								.toString());
					}
				}

				qb.set(f.getGeneratedName(), fieldVal);
			}
		}
		return qb.getQuery();
	}

	/**
	 * Saves the persistent instance to the database "if changes are made
	 * on it". If no changes are made, no queries will be executed.
	 */
	public void update() {
		//TODO discuss: is persistency check required? 
		Query q = prepareUpdateQuery();
		MappingSession.getExecuter().executeOnly(q);

		makePersistent();
	}

	private Query prepareUpdateQuery() {
		List<Field> fields = getEntity().getFields();
		List<Field> updatedFields = getChangedFields(fields);

		if (!updatedFields.isEmpty()) {
			QueryBuilder qb = QueryBuilder.update().from(
					getEntity().getGeneratedName());
			for (Field f : updatedFields) {
				Object fieldVal = getEntityField(f, getEntity(), this);

				if (!f.isNullable() && fieldVal == null)
					throw new NotNullableFieldException(getEntity()
							.getOriginalFullName(), f.getOriginalName());

				if (fieldVal != null && fieldVal instanceof Model<?>) {
					Model<?> instance = (Model<?>) fieldVal;
					fieldVal = fieldValueSerializer(fieldVal);

					if (!instance.isPersistent()) {
						throw new UnableToSaveDetachedInstanceAsFieldException(f
								.getOriginalName(), instance.getClass()
								.toString());
					}
				}

				qb.set(f.getGeneratedName(), fieldVal);
			}
			qb.where(C.eq(getEntity().getIdField().getGeneratedName(),
					__persistencyId));
			return qb.getQuery();
		} else
			return null;
	}

	private List<Field> getChangedFields(List<Field> allFields) {
		List<Field> updatedFields = new ArrayList<Field>();

		for (int i = 0; i < __persistencyFieldHashes.length; i++) {
			Object o = getEntityField(allFields.get(i), getEntity(), this);
			if (((o == null) ? DEFAULT_TRANSIENT_HASHCODE : o.hashCode()) != __persistencyFieldHashes[i])
				updatedFields.add(allFields.get(i));
		}

		return updatedFields;
	}

	/**
	 * Deletes current instance from the database and makes it transient.
	 * 
	 * Precondition: instance is persistent.
	 * Postcondition: instance is transient (non-persistent).
	 * 
	 * @throws UnableToPersistDetachedEntityException if the instance which is being
	 * attempted to be saved is not persistent (or detached).
	 * 
	 */
	public void delete() {
		if (!isPersistent())
			throw new UnableToPersistDetachedEntityException(this.getEntity()
					.getOriginalFullName());

		Query q = prepareDeleteQuery();
		MappingSession.getExecuter().executeOnly(q);

		makeTransient();
	}

	/**
	 * Counts all rows of this type of entity.
	 * 
	 * @return row count
	 */
	public int countAll() {
		ModelQuery mq = ModelQuery.type(QueryType.SELECT).from(getEntity())
				.count();
		Query q = mq.getQuery();

		return (Integer) MappingSession.getExecuter().executeForSingleValue(q);
	}

	private Query prepareDeleteQuery() {
		return QueryBuilder.delete().from(getEntity().getGeneratedName())
				.where(
						C.eq(getEntity().getIdField().getGeneratedName(),
								__persistencyId)).getQuery();
	}

	/**
	 * @return value of {@link Id} field of this domain class.
	 */
	private Object getEntityId() {
		Field idField = getEntity().getIdField();
		return getEntityField(idField, getEntity(), this);
	}

	/**
	 * Set the {@link Model} flags and hashes using the persistent fields of the
	 * entity to detect that object is changed later.
	 */
	private void makePersistent() {
		List<Field> fields = getEntity().getFields();
		__persistencyFieldHashes = new int[fields.size()];

		for (int i = 0; i < __persistencyFieldHashes.length; i++) {
			Object o = getEntityField(fields.get(i), getEntity(), this);
			__persistencyFieldHashes[i] = (o == null) ? DEFAULT_TRANSIENT_HASHCODE
					: o.hashCode();
		}
		__persistencyId = getEntityId();
		__persistencyHash = this.hashCode();
	}

	/**
	 * Breaks the persistency of the {@link Model} instance by changing its
	 * persistentcy id, hash and all field hashes. Can be used after
	 * <code>delete()</code> method to detach instance so that it can not invoke
	 * <code>update()</code>.
	 */
	private void makeTransient() {
		List<Field> fields = getEntity().getFields();
		__persistencyFieldHashes = new int[fields.size()];
		__persistencyId = DEFAULT_TRANSIENT_HASHCODE;
		__persistencyHash = DEFAULT_TRANSIENT_HASHCODE;
	}

	/**
	 * 
	 * Note that return value to be casted field.getClazz().
	 * 
	 * @return value of given {@link Field} of some {@link Model} instance.
	 */
	private Object getEntityField(Field field, Entity of, Model<E> instance) {
		Method getter = field.getGetterMethod();

		if (getter == null) { // field is already public
			try {
				return of.getClazz().getDeclaredField(field.getOriginalName())
						.get(instance);
			} catch (Exception e) {
				// TODO caution: assuming field certainly exists and accessible.
				e.printStackTrace(); // TODO LOG
			}
		} else { // not public field, invoke method!
			try {
				return getter.invoke(instance);
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and
				// accessible.
				e.printStackTrace(); // TODO log
			}
		}
		return null;
	}

	/**
	 * Sets the given {@link Field} of some {@link Model} instance with given
	 * <code>value</code>.
	 * 
	 * Various exceptions may thrown if unsuitable <code>value</code> is passed
	 * or <code>field</code> does not belong to the {@link Entity}.
	 */
	private void setEntityField(Field field, Entity of, Model<E> instance,
			Object value) {
		Method setter = field.getSetterMethod();

		if (setter == null) { // field is already public
			try {
				of.getClazz().getDeclaredField(field.getOriginalName()).set(
						instance, value);
			} catch (Exception e) {
				// TODO caution: assuming field certainly exists and accessible.
				e.printStackTrace(); // TODO log
			}
		} else { // not public field, invoke method!
			try {
				setter.invoke(instance, value); // no need to hold return value
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and
				// accessible.
				e.printStackTrace(); // TODO log
			}
		}
	}

	/**
	 * Used to return {@link Id}s of instance if <code>fieldVal</code> is an
	 * {@link org.orman.mapper.annotation.Entity}, return normal value
	 * otherwise.
	 */
	public static Object fieldValueSerializer(Object fieldVal) {
		if (fieldVal == null)
			return null;
		if (MappingSession.entityExists(fieldVal.getClass())) {
			return ((Model<?>) fieldVal).getEntityId();
		}
		return fieldVal;
	}

	/**
	 * Hashcode implementation for persistent entitites. Uses non-
	 * <code>transient</code> fields of the entity to compute some hash code.
	 * Can be used to detect whether some field is changed.
	 */
	@Override
	public int hashCode() {
		int hash = 1;

		for (Field f : getEntity().getFields()) {
			Object o = getEntityField(f, getEntity(), this);

			if (!(o instanceof Model<?>)) //TODO FATAL Model instances are not counted in hashcode!!! do something!!!
				hash += (o == null ? 1 : o.hashCode()) * 7; // magic.
		}

		// prevent coincidently correspontransiency flag
		return hash == DEFAULT_TRANSIENT_HASHCODE ? hash | 0x9123 : hash;

	}

	public Class<?> getType() {
		return clazz;
	}
}
