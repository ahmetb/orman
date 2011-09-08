package org.orman.mapper.annotation.inspector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.orman.dbms.PackageEntityInspector;
import org.orman.util.logging.Log;

import dalvik.system.DexFile;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Automatic @{@link Entity} annotated class finder from a given package for Android. 
 * Uses Dalvik Dex Entry Scanning
 * 
 * @author oguz kartal <0xffffffff@oguzkartal.net>
 */

public class AndroidPackageEntityInspector implements PackageEntityInspector{
	private Context context;
	
	public AndroidPackageEntityInspector(Context context) {
		this.context = context;
	}
	
	private static DexClassLoader getDexLoader(Context context, String apk) {
		PathClassLoader pathLoader = null;
		DexClassLoader dexLoader = null;
		
		//Google recommends following dex output usage
		String dexOutDir = context.getDir("dex", 0).getAbsolutePath();
		
		pathLoader = new PathClassLoader(apk,ClassLoader.getSystemClassLoader());
		
		dexLoader = new DexClassLoader(apk,dexOutDir,null,pathLoader);
		
		return dexLoader;
	}
	
	private static List<Class<?>> findEntitiesInPackage(Object appContext, String packageName) {
		
		if (!(appContext instanceof Context)) {
			Log.error("appContext is not a valid context");
			return null;
		}
		
		Context context = (Context)appContext;
		String appRootPackage = context.getPackageName();
		String apkFilename;
		DexFile appDexFile;
		DexClassLoader classLdr;
		List<Class<?>> annotatedEntities = null;
		Enumeration<String> dexEntries;
		
		try {
			apkFilename = context.getPackageManager().getApplicationInfo(appRootPackage, 0).sourceDir;
		} catch (NameNotFoundException e) {
			Log.warn("apk file could not detect for %s", appRootPackage);
			return null;
		}
		
		try {
			appDexFile = new DexFile(apkFilename);
		} catch (IOException e) {
			Log.error("An error occurred while looking dex file. Error: %s", e.getMessage());
			return null;
		}
		
		classLdr = getDexLoader(context, apkFilename);
		
		dexEntries = appDexFile.entries();
		
		if (!dexEntries.hasMoreElements()) {
			Log.error("appDexFile (%s) has not any entry", appDexFile.getName());
			return null;
		}
		
		annotatedEntities = new ArrayList<Class<?>>();
		
		while (dexEntries.hasMoreElements()) {
			String entry = dexEntries.nextElement();
			
			if (entry.startsWith(packageName) || packageName == null) {
				Class<?> clazz = null;
				try {
					
					clazz = classLdr.loadClass(entry);
					
				} catch (Exception e) {
					Log.fatal("The Loader could not load %s->'%s'",packageName,entry);
				}
				
				if (clazz != null && GenericAnnotation.
										isAnnotationPresent(clazz, org.orman.mapper.annotation.Entity.class)) {
					annotatedEntities.add(clazz);
				}
			}
		}
		
		return annotatedEntities.size() == 0 ? null : annotatedEntities;
	}

	@Override
	public List<Class<?>> findEntitiesInPackage(String packageName) {
		return findEntitiesInPackage(this.context,packageName);
	}

	@Override
	public String getWorkingRootPackageName() {
		//Always returns null
		return null;
	}
}