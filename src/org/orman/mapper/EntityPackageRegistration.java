package org.orman.mapper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.orman.util.logging.Log;

/**
 * Auto registration system for annotated entity classes
 * @author 0ffffffffh
 */
public class EntityPackageRegistration {
	
	@SuppressWarnings("rawtypes")
	private static Class getClassFor(File classFile, String packageName) {
		String fileName = classFile.getName();
		Class classObj = null;
		
		if (fileName.endsWith(".class")) {
			try {
				classObj = Class.forName(packageName + "." + fileName.substring(0, fileName.length()-6));
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return classObj;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<Class> populateClasses(File objectDir, String packageName) {
		List<Class> classes = new ArrayList<Class>();
		File[] classFiles = objectDir.listFiles();
		
		for (File classFile : classFiles) {
			if (!classFile.isDirectory())
				classes.add(getClassFor(classFile, packageName));
			else
				classes.addAll(populateClasses(classFile,packageName));
		}
		
		return classes;		
	}
	
	/**
	 * Registers all annotated classes from given package.
	 * @param packageName Entity container package
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int registerAllEntityClassesInPackage(String packageName) {
		int registeredEntities=0;
		String packageUrl,fileName;
		URL element;
		Enumeration<URL> classResources;
		
		List<Class> classObjects;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		if (classLoader == null)
			return -1;
		
		packageUrl = packageName.replace('.', '/');
		
		try {
			classResources = classLoader.getResources(packageUrl);
		} catch (IOException e) {
			Log.error(e.getMessage());
			return -1;
		}
		
		classObjects = new ArrayList<Class>();
		
		while (classResources.hasMoreElements()) {
			element = classResources.nextElement();
			
			try {
				fileName = URLDecoder.decode(element.getFile(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				try {
					fileName = URLDecoder.decode(element.getFile(),"ISO-8859-1");
				} catch (UnsupportedEncodingException e1) {
					fileName = element.getFile().replace("%20", " ");
				}
			}
		
			
			classObjects = populateClasses(new File(fileName),packageName);
			
			for (Class currentClass : classObjects) {
				if (currentClass.isAnnotationPresent(org.orman.mapper.annotation.Entity.class)) {
					MappingSession.registerEntity(currentClass);
					registeredEntities++;
				}
			}
			
		}
		
		return registeredEntities;
	}
}
