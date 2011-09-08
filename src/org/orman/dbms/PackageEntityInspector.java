package org.orman.dbms;

import java.util.List;

/**
 * Generic entity inspector interface
 * 
 * @author oguz kartal <0xffffffff@oguzkartal.net>
 */
public interface PackageEntityInspector {
	public List<Class<?>> findEntitiesInPackage(String packageName);
	public String getWorkingRootPackageName();
}
