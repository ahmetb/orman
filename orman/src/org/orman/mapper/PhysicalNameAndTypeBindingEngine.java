package org.orman.mapper;

public class PhysicalNameAndTypeBindingEngine {
	private static final String INDEX_POSTFIX = "_index";

	public static void makeBinding(Entity entity, PhysicalNamingPolicy namingPolicy){
		if(entity.getCustomName() != null){
			// use custom name.
			entity.setGeneratedName(entity.getCustomName());
		} else {
			// format name accordingly naming policy
			entity.setGeneratedName(PhysicalNameGenerator.format(entity.getOriginalName(), namingPolicy));
		}
	}
	
	public static void makeBinding(Field field, PhysicalNamingPolicy namingPolicy, DataTypeMapper dataTypeMapper){
		/* NAME BINDING */
		if(field.getCustomName() != null){
			// use custom name.
			field.setGeneratedName(field.getCustomName());
		} else {
			// format name accordingly naming policy.
			field.setGeneratedName(PhysicalNameGenerator.format(field.getOriginalName(), namingPolicy));
		}
		
		/* TYPE BINDING */
		if(field.getCustomType() != null){
			// use manual field type.
			field.setType(field.getCustomType());
		} else {
			// use Class<?> type of the field.
			field.setType(dataTypeMapper.getTypeFor(field.getClazz()));
		}
		
		/* INDEX SETTINGS BINDING */
		if(field.getIndex() != null){
			if(field.getIndex().name() == null || "".equals(field.getIndex().name())){
				// missing index name, create using field name followed by _index policy.
				field.getIndex().name(field.getGeneratedName()+INDEX_POSTFIX);
			}
		}
	}
}