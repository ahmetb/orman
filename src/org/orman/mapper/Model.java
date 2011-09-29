package org.orman.mapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.orman.dbms.ResultList;
import org.orman.mapper.annotation.PrimaryKey;
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
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
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

	protected Entity getEntity() {
		if (__mappedEntity == null) {
			__mappedEntity = MappingSession.getEntity(this.clazz);
		}
		return __mappedEntity;
	}

	/**
	 * Can be used to find that the instance is exactly as its state when saved
	 * in terms of persistence.
	 * 
	 * Detached instances are not saved at all or changed after saving or
	 * fetching from database.
	 * 
	 * @return false if object is changed or not saved at all, true if the
	 *         object is saved.
	 */
	public boolean isPersistent() {
		return (this.hashCode() == __persistencyHash)
				&& __persistencyHash != DEFAULT_TRANSIENT_HASHCODE;
	}

	public static <E extends Model<E>> int bulkInsert(Class<E> clazz,
			String sourceFile, String regEx) {
		BulkInsert<E> bulkOp = new BulkInsert<E>(clazz, sourceFile, regEx);
		return bulkOp.startBulkInsert();
	}

	public static <E extends Model<E>> int bulkInsert(Class<E> clazz,
			String sourceFile, String fieldSeperator, String rowSeperator) {
		BulkInsert<E> bulkOp = new BulkInsert<E>(clazz, sourceFile,
				fieldSeperator, rowSeperator);
		return bulkOp.startBulkInsert();
	}

	/**
	 * Inserts the instance to the database as row and then binds generated id
	 * if IdGenerationPolicy is DEFER_TO_DBMS.
	 * 
	 * Postcondition: Instance is persistent.
	 */
	public void insert() {
		// TODO discuss: persistence check?

		Query q = prepareInsertQuery();
		MappingSession.getExecuter().executeOnly(q);

		// Bind last insert id if IdGenerationPolicy is DEFER_TO_DBMS
		// if an auto increment field exists.
		if (MappingSession.getConfiguration().getIdGenerationPolicy()
				.equals(IdGenerationPolicy.DEFER_TO_DBMS)) {
			Field autoIncrField = getEntity().getAutoIncrementField();

			if (autoIncrField != null) {
				setEntityField(autoIncrField, getEntity(), MappingSession
						.getExecuter()
						.getLastInsertId(autoIncrField.getClazz()));
			}

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
			// TODO CRITICAL: test this for multi primary keys!
			if (f.isPrimaryKey() && f.isAutoIncrement()) {
				IdGenerationPolicy policy = MappingSession.getConfiguration()
						.getIdGenerationPolicy();

				if (policy == IdGenerationPolicy.ORMAN_ID_GENERATOR)
					/* bind generated id to the transient instance */
					setEntityField(f, getEntity(),
							NativeIdGenerator.generate(f, this));
				if (policy == IdGenerationPolicy.DEFER_TO_DBMS)
					useField = false;
			}

			useField = useField && !f.isList(); // list fields are not
												// physically created.

			if (useField) { // use field in query
				Object fieldVal = getEntityField(f);

				if (!f.isNullable() && fieldVal == null) // prevent saving null
															// on @NotNull
					throw new NotNullableFieldException(getEntity()
							.getOriginalFullName(), f.getOriginalName());

				if (fieldVal != null && fieldVal instanceof Model<?>) {
					Model<?> instance = (Model<?>) fieldVal;

					fieldVal = fieldValueSerializer(fieldVal);

					if (!instance.isPersistent()) {
						throw new UnableToSaveDetachedInstanceAsFieldException(
								f.getOriginalName(), instance.getClass()
										.toString());
					}
				}

				qb.set(f.getGeneratedName(), fieldVal);
			}
		}
		return qb.getQuery();
	}

	/**
	 * Saves the persistent instance to the database "if changes are made on
	 * it". If no changes are made, no queries will be executed.
	 */
	public void update() {
		// TODO discuss: is persistency check required?
		Query q = prepareUpdateQuery();
		if (q != null)
			MappingSession.getExecuter().executeOnly(q);

		// TODO discuss: what should be done if list fields are updated.
		makePersistent();
	}

	private Query prepareUpdateQuery() {
		List<Field> fields = getEntity().getFields();
		List<Field> updatedFields = getChangedFields(fields);
		
		if (!updatedFields.isEmpty()) {
			QueryBuilder qb = QueryBuilder.update().from(
					getEntity().getGeneratedName());
			for (Field f : updatedFields) {
				Object fieldVal = getEntityField(f);

				if (!f.isNullable() && fieldVal == null)
					throw new NotNullableFieldException(getEntity()
							.getOriginalFullName(), f.getOriginalName());

				if (fieldVal != null && fieldVal instanceof Model<?>) {
					Model<?> instance = (Model<?>) fieldVal;
					fieldVal = fieldValueSerializer(fieldVal);

					if (!instance.isPersistent()) {
						throw new UnableToSaveDetachedInstanceAsFieldException(
								f.getOriginalName(), instance.getClass()
										.toString());
					}
				}

				qb.set(f.getGeneratedName(), fieldVal);
			}
			qb.where(C.eq(getEntity().getAutoIncrementField()
					.getGeneratedName(), __persistencyId)); // TODO discuss:
															// what about
															// entities without
															// @Id?
															// (__persistencyId)
			return qb.getQuery();
		} else
			return null;
	}

	private List<Field> getChangedFields(List<Field> allFields) {
		// if field hashes are null, then the field is not 
		// persisted at all.
		if (__persistencyFieldHashes == null) {
			throw new UnableToPersistDetachedEntityException(this.getEntity()
					.getOriginalFullName());
		}

		List<Field> updatedFields = new ArrayList<Field>();

		for (int i = 0; i < __persistencyFieldHashes.length; i++) {
			Object o = getEntityField(allFields.get(i));
			
			if (o instanceof EntityList){
				continue;
				// because entity lists are always persistent and 
				// executes add/remove etc. immediately.
			}
			
			if (((o == null) ? DEFAULT_TRANSIENT_HASHCODE : o.hashCode()) != __persistencyFieldHashes[i])
				updatedFields.add(allFields.get(i));
		}

		return updatedFields;
	}

	/**
	 * Deletes current instance from the database and makes it transient.
	 * 
	 * Precondition: instance is persistent. Postcondition: instance is
	 * transient (non-persistent).
	 * 
	 * @throws UnableToPersistDetachedEntityException
	 *             if the instance which is being attempted to be saved is not
	 *             persistent (or detached).
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
		return QueryBuilder
				.delete()
				.from(getEntity().getGeneratedName())
				.where(C.eq(getEntity().getAutoIncrementField()
						.getGeneratedName(), __persistencyId)).getQuery();
	}

	/**
	 * TODO CRITICAL: test & fix after Id depreciation.
	 * 
	 * @return value of {@link PrimaryKey} field of this domain class.
	 */
	private Object getEntityId() {
		Field idField = getEntity().getAutoIncrementField();
		return getEntityField(idField);
	}

	/**
	 * Set the {@link Model} flags and hashes using the persistent fields of the
	 * entity to detect that object is changed later.
	 */
	protected void makePersistent() {
		List<Field> fields = getEntity().getFields();
		__persistencyFieldHashes = new int[fields.size()];

		for (int i = 0; i < __persistencyFieldHashes.length; i++) {
			Field f = fields.get(i);
			Object o = getEntityField(f);
			__persistencyFieldHashes[i] = (o == null || f.isList()) ? DEFAULT_TRANSIENT_HASHCODE
					: o.hashCode();
		}
		if (getEntity().getAutoIncrementField() != null)
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
	protected Object getEntityField(Field field) {
		Method getter = field.getGetterMethod();

		if (getter == null) { // field is already public
			try {
				return getEntity().getType()
						.getDeclaredField(field.getOriginalName()).get(this);
			} catch (Exception e) {
				// TODO caution: assuming field certainly exists and accessible.
				e.printStackTrace(); // TODO LOG
			}
		} else { // not public field, invoke method!
			try {
				return getter.invoke(this);
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
	protected void setEntityField(Field field, Entity of, Object value) {
		Method setter = field.getSetterMethod();

		if (setter == null) { // field is already public
			try {
				of.getType().getDeclaredField(field.getOriginalName())
						.set(this, value);
			} catch (Exception e) {
				// TODO caution: assuming field certainly exists and accessible.
				e.printStackTrace(); // TODO log
			}
		} else { // not public field, invoke method!
			try {
				setter.invoke(this, value); // no need to hold return value
			} catch (Exception e) {
				// TODO caution: assuming getter certainly exists and
				// accessible.
				e.printStackTrace(); // TODO log
			}
		}
	}

	/**
	 * Used to return id of instance if <code>fieldVal</code> is an
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
			if (f.isList())
				continue; // TODO list fields are not counted.
			Object o = getEntityField(f);

			if (!(o instanceof Model<?>)) // TODO FATAL Model instances are not
											// counted in hashcode!!! do
											// something!!!
				hash += (o == null ? 1 : o.hashCode()) * 7; // magic.
		}

		// prevent coincidently correspontransiency flag
		return hash == DEFAULT_TRANSIENT_HASHCODE ? hash | 0x9123 : hash;
	}

	/**
	 * Executes the query and returns the rows mapped to entities as a list.
	 * 
	 * CAUTION: If the query does not have SELECT * or has JOINs, or anything
	 * that can break the field order as they are declared in the class.
	 * 
	 * @param q
	 *            query generated with {@link ModelQuery}.
	 * @param intendedType
	 *            instances will be casted to this type.
	 * @return set of results. never null.
	 */
	public static <E> List<E> fetchQuery(Query q, Class<E> intendedType) {
		Entity e = MappingSession.getEntity(intendedType);
		List<E> mappedRecordList = new ArrayList<E>();

		ResultList resultList = MappingSession.getExecuter()
				.executeForResultList(q);

		if (resultList != null) {
			// something is returned, do the reverse mapping and add to the
			// list.
			for (int i = 0; i < resultList.getRowCount(); i++) {
				mappedRecordList.add(ReverseMapping.map(
						resultList.getResultRow(i), intendedType, e));
			}
		}
		return mappedRecordList;
	}

	/**
	 * Executes the query and returns the "first" result mapped to the entity
	 * instance. Reverse mapping is done for <code>type</code> parameter.
	 * 
	 * CAUTION: If the query does not have SELECT * or has JOINs, or anything
	 * that can break the field order as they are declared in the class.
	 * 
	 * @param q
	 *            query generated with {@link ModelQuery}.
	 * @return null if no results are found, an instance if some results are
	 *         successfully retrieved.
	 */
	public static <E> E fetchSingle(Query q, Class<E> type) {
		List<E> l = fetchQuery(q, type);

		if (l == null || l.size() == 0)
			return null;
		return l.get(0);
	}

	/**
	 * Executes the query and returns the single value as String object.
	 * Conversion left to the user.
	 * 
	 * @param q
	 *            query generated with {@link ModelQuery}.
	 * @param type
	 *            preferably a primitive type.
	 * @return
	 */
	public static Object fetchSingleValue(Query q) {
		Object o = MappingSession.getExecuter().executeForSingleValue(q);
		return o;
	}
	
	/**
	 * Finds all instances of a given @{@code Entity}-annotated class type.
	 * 
	 * @param ofEntity entity class type
	 * @return non-empty list of result list. never <code>null</code>.
	 */
	public static <E> List<E> fetchAll(Class<E> ofEntity){
		return fetchQuery(ModelQuery.select().from(ofEntity).getQuery(), ofEntity);
	}

	/**
	 * Executes given query for no result, i.e. it can be used for
	 * <code>DELETE</code>, <code>UPDATE</code> queries.
	 * 
	 * @param q
	 *            query generated with {@link ModelQuery}.
	 */
	public static void execute(Query q) {
		MappingSession.getExecuter().executeOnly(q);
	}

	public Class<?> getType() {
		return clazz;
	}
}
