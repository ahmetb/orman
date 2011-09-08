package org.orman.mapper.annotation.inspector;

import java.lang.annotation.Annotation;


public class GenericAnnotation {
	
	private static Annotation getAnnotationFor(Class<?> clazz, Class<? extends Annotation> annotation) {
		Annotation[] annots = null;
		
		if (clazz.isAnnotationPresent(annotation)) {
			return clazz.getAnnotation(annotation);
		}
		
		annots = clazz.getAnnotations();
		
		for (Annotation annot : annots) {
			String annName = annotation.getName();
			if (annName.equals(annot.annotationType().getName())) {
				return annot;
			}
		}
		
		return null;
	}
	
	public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation) {
		if (!clazz.isAnnotationPresent(annotation)) {
			
			Annotation[] annots = clazz.getAnnotations();
			
			for (Annotation annot : annots) {
				String annName = annotation.getName();
				if (annName.equals(annot.annotationType().getName())) {
					return true;
				}
			}
			
			return false;
			
		}
		
		return true;
	}
	
	
}