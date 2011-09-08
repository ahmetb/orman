package org.orman.mapper.annotation.inspector;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.orman.dbms.PackageEntityInspector;
import org.orman.mapper.Entity;
import org.orman.util.logging.Log;

/**
 * Automatic @{@link Entity} annotated class finder from a given package. Uses
 * classpath scanning.
 * 
 * @author oguz kartal <0xffffffff@oguzkartal.net>
 */
public class PackageEntityInspectorImpl implements PackageEntityInspector {
	private static boolean recursiveScan = true;
	
	@SuppressWarnings("rawtypes")
	private static Class<?> getClassFor(File classFile, String packageName) {
		String fileName = classFile.getName();
		String className;
		Class classObj = null;

		if (fileName.endsWith(".class")) {
			try {
				className = packageName != null ? packageName + "." : "";
				className += fileName.substring(0,fileName.length()-6);
				
				classObj = Class.forName(className);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return classObj;
	}
	
	private static String getInnerClassPath(File classPath) {
		String path = classPath.getAbsolutePath();
		int i = path.lastIndexOf(File.separator);
		
		if (i != -1) {
			return path.substring(i+1,path.length());
		}
		
		return path;
	}
	
	private static List<Class<?>> populateClasses(File objectDir,String packageName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File[] classFiles = objectDir.listFiles();
		Class<?> clazz = null;
		
		if (classFiles != null && classFiles.length > 0) { // prevent npe
			for (File classFile : classFiles) {
				if (!classFile.isDirectory()) {
					clazz = getClassFor(classFile,packageName);
					if (clazz != null) {
						classes.add(clazz);
					}
					else {
						Log.warn("Could not load class object for %s->%s",
								classFile.getAbsolutePath(),
								packageName);
					}
				}
				else if (recursiveScan)
					classes.addAll(populateClasses(classFile, packageName + "." + getInnerClassPath(classFile)));
			}
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
	public static List<Class<?>> _findEntitiesInPackage(String packageName) {
		String packageUrl = null, fileName;
		URL element;
		Enumeration<URL> classResources;

		List<Class<?>> classObjects;
		List<Class<?>> annotatedClasses = null;

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		if (classLoader == null)
			return null;
		
		if (packageName == null)
			packageUrl = "./"; //Search into root class path
		else 
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
	
	@Override
	public String getWorkingRootPackageName() {
		int i;
		
		StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
		String rootClass = callStack[callStack.length-1].getClassName();
		
		i = rootClass.lastIndexOf('.');
		
		if (i == -1) {
			recursiveScan = false;
			return null;
		}
		
		return rootClass.substring(0,i);
	}

	@Override
	public List<Class<?>> findEntitiesInPackage(String packageName) {
		return _findEntitiesInPackage(packageName);
	}
}
