package org.orman.mapper;

import org.orman.datasource.DataTypeMapper;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.OneToOne;

/**
 * Provides methods to make physical name and column type bindings to fields and
 * physiscal table name bindings for the classes.
 * 
 * @author alp
 * 
 */
public class PhysicalNameAndTypeBindingEngine {
	private static final String INDEX_POSTFIX = "index";
	private static final String INDEX_FORMAT = "%s_%s_%s";

	public static void makeBinding(Entity entity,
			PhysicalNamingPolicy namingPolicy) {
		if (entity.getCustomName() != null) {
			// use custom name.
			entity.setGeneratedName(entity.getCustomName());
		} else {
			// format name accordingly naming policy
			entity.setGeneratedName(PhysicalNameGenerator.format(entity
					.getOriginalName(), namingPolicy));
		}
	}

	/**
	 * Makes name and column type bindings to given <code>field</code>. It also
	 * makes default name bindings to unnamed {@link Index}es (or {@link Id} fields
	 * since it covers {@link Index})
	 * 
	 * @param entity used to generate index name if not specified.
	 * @param field
	 * @param namingPolicy
	 * @param dataTypeMapper
	 */
	public static void makeBinding(Entity entity, Field field,
			PhysicalNamingPolicy namingPolicy, DataTypeMapper dataTypeMapper) {
		/* NAME BINDING */
		if (field.getCustomName() != null) {
			// use custom name.
			field.setGeneratedName(field.getCustomName());
		} else {
			// format name accordingly naming policy.
			field.setGeneratedName(PhysicalNameGenerator.format(field
					.getOriginalName(), namingPolicy));
		}

		/* DATA TYPE BINDING */
		if (field.getCustomType() != null) {
			// use manual field type.
			field.setType(field.getCustomType());
		} else {
			Class<?> fieldType = null;
			// use Class<?> type of the field.
			
			
			if (field.isAnnotationPresent(OneToOne.class)){
				//  there exists 1:1 set type as matched field's Id type.
				Class<?> idType = getIdTypeForClass(field.getClazz());
				fieldType = idType;
			} else if(MappingSession.entityExists(field.getClazz())){
				// infer entity @Id type if @*to* annotations does not exist 
				Class<?> idType = getIdTypeForClass(field.getClazz());
				fieldType = idType;
			} else {
				// usual conditions
				fieldType = field.getClazz();
			}
			
			field.setType(dataTypeMapper.getTypeFor(fieldType));
		}

		/* INDEX SETTINGS BINDING */
		if (field.getIndex() != null) {
			if (field.getIndex().name() == null
					|| "".equals(field.getIndex().name())) {
				// missing index name, create using field name followed by
				// _index policy.
				field.getIndex().name(
						String.format(INDEX_FORMAT, entity.getGeneratedName(),
								field.getGeneratedName(), INDEX_POSTFIX));
			}
		}
	}

	private static Class<?> getIdTypeForClass(Class<?> clazz) {
		for(java.lang.reflect.Field f : clazz.getDeclaredFields())
			if(f.isAnnotationPresent(Id.class))
				return f.getType();
		return null;
	}
}