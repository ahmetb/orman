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
 * Automatic @{@link Entity} annotated class finder from a given package. Uses
 * classpath scanning.
 * 
 * @author 0ffffffffh
 */
public class PackageEntityInspector {

	@SuppressWarnings("rawtypes")
	private static Class<?> getClassFor(File classFile, String packageName) {
		String fileName = classFile.getName();
		Class classObj = null;

		if (fileName.endsWith(".class")) {
			try {
				classObj = Class.forName(packageName + "."
						+ fileName.substring(0, fileName.length() - 6));
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return classObj;
	}

	private static List<Class<?>> populateClasses(File objectDir,
			String packageName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File[] classFiles = objectDir.listFiles();

		for (File classFile : classFiles) {
			if (!classFile.isDirectory())
				classes.add(getClassFor(classFile, packageName));
			else
				classes.addAll(populateClasses(classFile, packageName));
		}

		return classes;
	}

	/**
	 * Returns list of {@link Class} types of available classes types annotated
	 * with @{@link Entity} from given package, recursively.
	 * 
	 * @param packageName
	 *            Entity container package
	 * @return List of all annotated classes
	 */
	public static List<Class<?>> findEntitiesInPackage(String packageName) {
		String packageUrl, fileName;
		URL element;
		Enumeration<URL> classResources;

		List<Class<?>> classObjects;
		List<Class<?>> annotatedClasses = null;

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		if (classLoader == null)
			return null;

		packageUrl = packageName.replace('.', '/');

		try {
			classResources = classLoader.getResources(packageUrl);
		} catch (IOException e) {
			Log.error(e.getMessage());
			return null;
		}

		classObjects = new ArrayList<Class<?>>();

		while (classResources.hasMoreElements()) {
			element = classResources.nextElement();

			try {
				fileName = URLDecoder.decode(element.getFile(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				try {
					fileName = URLDecoder.decode(element.getFile(),
							"ISO-8859-1");
				} catch (UnsupportedEncodingException e1) {
					fileName = element.getFile().replace("%20", " ");
				}
			}

			annotatedClasses = populateClasses(new File(fileName), packageName);
			
			for (Class<?> currentClass : annotatedClasses) {
				// currentClass becomes null for non-Java files in package. 
				// and may throw NullPointerException. make null-check.
				if (currentClass != null && currentClass
						.isAnnotationPresent(org.orman.mapper.annotation.Entity.class)) {
					classObjects.add(currentClass);
				}
			}
		}

		return classObjects.size() == 0 ? null : classObjects;
	}
	
	public static String getWorkingRootPackageName() {
		int i;
		
		StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
		String rootClass = callStack[callStack.length-1].getClassName();
		
		i = rootClass.indexOf('.');
		
		if (i == -1)
			return rootClass;
		
		return rootClass.substring(0,i);
	}
}
