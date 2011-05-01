package org.orman.sql.util;

import java.util.Collection;

import org.orman.sql.Criterion;
import org.orman.sql.Query;
import org.orman.sql.StringLiteral;

public class Glue {
	
	public static String concat(Query[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i].nest());
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concat(String[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(new StringLiteral(a[i]).toString());
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concatNoEscape(String[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i]);
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concat(Object[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i].toString());
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}

	public static String concat(Collection<?> a, String glue){
		if (a == null) return null;
		int size = a.size();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for(Object o : a){
			sb.append(o.toString());
			
			if(i != size-1) sb.append(glue);
			i++;
		}
		return sb.toString();
	}
	
	public static String concat(int[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i]);
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concat(float[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i]);
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concat(double[] a, String glue){
		if (a == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < a.length; i++){
			sb.append(a[i]);
			if(i != a.length-1) sb.append(glue);
		}
		return sb.toString();
	}
	
	public static String concat(Criterion[] crit, String glue){
		if (crit == null) return null;
		StringBuilder sb = new StringBuilder();
		for(int i  = 0 ; i < crit.length; i++){
			sb.append(crit[i].nest());
			if(i != crit.length-1) sb.append(glue);
		}
		return sb.toString();
	}
}