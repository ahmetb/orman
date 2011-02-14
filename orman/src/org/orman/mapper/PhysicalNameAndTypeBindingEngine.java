package org.orman.mapper;

public class PhysicalNameAndTypeBindingEngine {
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
		// NAME BINDING
		if(field.getCustomName() != null){
			// use custom name.
			field.setGeneratedName(field.getCustomName());
		} else {
			// format name accordingly naming policy.
			field.setGeneratedName(PhysicalNameGenerator.format(field.getOriginalName(), namingPolicy));
		}
		
		// TYPE BINDING
		if(field.getCustomType() != null){
			// use manual field type.
			field.setType(field.getCustomType());
		} else {
			// use Class<?> type of the field.
			field.setType(dataTypeMapper.getTypeFor(field.getClazz()));
		}
	}
}