package org.orman.mapper;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
				fields.add(new Field(f.getType(), f.getName()));
			}
		}
		return this.fields;
	}
	
	public List<Field> getFields(){
		if(this.fields.isEmpty()) return extractFields();
		else return this.fields;
	}
}
