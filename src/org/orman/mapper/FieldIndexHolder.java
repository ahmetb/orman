package org.orman.mapper;

import org.orman.sql.IndexType;

/**
 * Holds index information of a {@link Field} such as 
 * its name and uniqueness. 
 * @author alp
 */
public class FieldIndexHolder {
	private boolean unique;
	private String name;
	private IndexType type;
	
	
	public FieldIndexHolder(String name, boolean unique){
		this(name, unique, IndexType.HASH);
	}
	
	public FieldIndexHolder(String name, boolean unique, IndexType type){
		this.name = name;
		this.unique = unique;
		this.type = type;
	}
	
	public boolean unique(){
		return this.unique;
	}
	
	public void unique(boolean val){
		this.unique = val;
	}
	
	public String name(){
		return this.name;
	}
	
	public void name(String s){
		this.name = s;
	}

	public void setType(IndexType type) {
		this.type = type;
	}

	public IndexType getType() {
		return type;
	}
	
}
