package org.orman.mapper;

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
