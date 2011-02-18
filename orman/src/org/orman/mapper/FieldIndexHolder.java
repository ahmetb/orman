package org.orman.mapper;

/**
 * Holds index information of a {@link Field} such as 
 * its name and uniqueness. 
 * @author alp
 */
public class FieldIndexHolder {
	private boolean unique;
	private String name;
	
	public FieldIndexHolder(String name, boolean unique){
		this.name = name;
		this.unique = unique;
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
	
}
