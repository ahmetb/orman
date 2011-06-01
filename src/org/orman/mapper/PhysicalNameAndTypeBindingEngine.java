package org.orman.mapper;

import org.orman.datasource.DataTypeMapper;
import org.orman.mapper.annotation.Column;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.OneToOne;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.mapper.exception.NotDeclaredIdException;
import org.orman.util.logging.Log;

/**
 * Provides methods to make physical name and column type bindings to fields and
 * physiscal table name bindings for the classes.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
public class PhysicalNameAndTypeBindingEngine {
	public static final String INDEX_POSTFIX = "Index";
	private static final String FIELD_INDEX_FORMAT = "%s%s%s";
	public static final String COMPOSITE_INDEX_FORMAT = "%s%s";

	public static void makeBinding(Entity entity,
			PhysicalNamingPolicy namingPolicy) {
		if (entity.getCustomName() != null) {
			// use custom name.
			Log.trace("Using custom name %s for entity %s.", entity.getCustomName(), entity.getOriginalName());
			entity.setGeneratedName(entity.getCustomName());
		} else {
			// format name accordingly naming policy
			entity.setGeneratedName(PhysicalNameGenerator.format(entity
					.getOriginalName(), namingPolicy));
		}
	}

	/**
	 * Makes name and column type bindings to given <code>field</code>. It also
	 * makes default name bindings to unnamed {@link Index}es (or autoIncrement
	 * {@link PrimaryKey} fields since it covers {@link Index})
	 * 
	 * @param entity
	 *            used to generate index name if not specified.
	 * @param field
	 * @param namingPolicy
	 * @param dataTypeMapper
	 */
	public static void makeBinding(Entity entity, Field field,
			PhysicalNamingPolicy namingPolicy, DataTypeMapper dataTypeMapper) {
		/* NAME BINDING */
		if (field.getCustomName() != null) {
			// use custom name.
			Log.trace("Using custom field name on field %s.%s.", entity.getOriginalName(), field.getOriginalName());
			field.setGeneratedName(field.getCustomName());
		} else {
			// format name accordingly naming policy.
			field.setGeneratedName(PhysicalNameGenerator.format(field
					.getOriginalName(), namingPolicy));
		}

		/* DATA TYPE BINDING */
		if (field.getCustomType() != null) {
			// use manual field type.
			Log.trace("Using custom type on field %s.%s.", entity.getOriginalName(), field.getOriginalName());
			field.setType(field.getCustomType());
		} else {
			Class<?> fieldType = null;
			// use Class<?> type of the field.
			
			String customizedBindingType = null;
			
			//TODO add OneToMany and ManyToOne and ManyToMany when eligible.
			if (field.isAnnotationPresent(OneToOne.class)){
				//  there exists 1:1 set type as matched field's Id type.
				Class<?> idType = getAutoIncrementTypeForClass(field.getClazz());
				fieldType = idType;
				
				// assign whether a customized field type exists with @Column.
				customizedBindingType = getCustomizedBinding(field.getClazz());
				
				Log.trace("OneToOne mapping detected on field %s.%s.", entity.getOriginalName(), field.getOriginalName());
			} else if(MappingSession.entityExists(field.getClazz())){
				// infer entity @PrimaryKey type if @*to* annotations does not exist 
				Class<?> idType = getAutoIncrementTypeForClass(field.getClazz());
				fieldType = idType;
				
				if (idType == null){
					throw new NotDeclaredIdException(field.getClazz().getName());
				}
				
				// assign whether a customized field type exists with @Column.
				customizedBindingType = getCustomizedBinding(field.getClazz());
				
				Log.trace("Direct entity mapping inferred on field %s.%s.",
						entity.getOriginalName(), field.getOriginalName());
			} else {
				// usual conditions
				fieldType = field.getClazz();
			}
			
			if (customizedBindingType != null) // there exists a binding with @Column(name=..)
				field.setType(customizedBindingType); 
			else
				field.setType(dataTypeMapper.getTypeFor(fieldType));
			
			Log.debug("Field '%s'(%s) mapped to '%s'(%s) using %s.", field
					.getOriginalName(), field.getClazz().getName(), field
					.getGeneratedName(), field.getType(),
					customizedBindingType != null ? "@Column annotation"
							: "type of @PrimaryKey field");
		}
		
		/* INDEX SETTINGS BINDING */
		if (field.getIndex() != null) {
			if (field.getIndex().name() == null
					|| "".equals(field.getIndex().name())) {
				// missing index name, create using field name followed by
				// "Index" policy.
				String indexName = null;
				
				if (field.isPrimaryKey()) {
					indexName = String.format(COMPOSITE_INDEX_FORMAT,
							PhysicalNameGenerator.capitalize(entity
									.getGeneratedName()), INDEX_POSTFIX);
				} else {
					indexName = String.format(FIELD_INDEX_FORMAT,
							PhysicalNameGenerator.capitalize(entity
									.getGeneratedName()), PhysicalNameGenerator
									.capitalize(field.getGeneratedName()),
							INDEX_POSTFIX);
				}
				Log.trace("Field '%s' index name binded as '%s'",
						field.getOriginalName(), indexName);
				field.getIndex().name(indexName);
			}
		}
	}
	
	private static String getCustomizedBinding(Class<?> clazz) {
		java.lang.reflect.Field idField = getAutoIncrementFieldForClass(clazz);
		if (idField == null) return null;
		if (idField.isAnnotationPresent(Column.class)){
			return idField.getAnnotation(Column.class).type();
		}
		return null;
	}

	private static java.lang.reflect.Field getAutoIncrementFieldForClass(Class<?> clazz) {
		for(java.lang.reflect.Field f : clazz.getDeclaredFields()){
			PrimaryKey pk = f.getAnnotation(PrimaryKey.class);
			if (pk != null){
				if (pk.autoIncrement()){
					return f;
				}
			}
		}
		return null;
	}

	private static Class<?> getAutoIncrementTypeForClass(Class<?> clazz) {
		java.lang.reflect.Field f = getAutoIncrementFieldForClass(clazz);
		if (f == null) return null;
		else return f.getType();
	}
}