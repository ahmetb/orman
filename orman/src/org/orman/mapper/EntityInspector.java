package org.orman.mapper;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.orman.mapper.annotation.Index;

public class EntityInspector {
	
	private Class<?> clazz;
	private List<Field> fields;
	
	public EntityInspector(Class<?> forClass){
		this.clazz = forClass;
		fields = new ArrayList<Field>();
	}


	private List<Field> extractFields(){
		this.fields.clear();
		
		for(java.lang.reflect.Field f : this.clazz.getDeclaredFields()){
			if(!Modifier.isTransient(f.getModifiers())){
				Field newF = new Field(f.getType(), f.getName());
				if(f.isAnnotationPresent(Index.class)){
					Index ann = f.getAnnotation(Index.class);
					newF.setIndex(new FieldIndexHolder(ann.name(), ann.unique()));
				}
				fields.add(newF);
			}
		}
		return this.fields;
	}
	
	public List<Field> getFields(){
		if(this.fields.isEmpty()) return extractFields();
		else return this.fields;
	}
}
