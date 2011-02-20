package org.orman.mapper;

/**
 * Determines how the schemes are designated on the <code>start()</code> of
 * {@link MappingSession}.
 * 
 * @author alp
 * 
 */
public enum SchemeCreationPolicy {
	/**
	 * Drops the table (with truncating its data) if exists and reconstructs the
	 * whole table from scratch each initialization process. WARNING: Loss of
	 * data.
	 * 
	 * Required on changes in the scheme.
	 */
	CREATE,

	/**
	 * Creates the tables ONLY if they do not exist. If they exists,
	 * it uses the existing database scheme. Does not DROP existing 
	 * tables and their data.
	 * 
	 * If physical scheme is not up to date, various runtime exceptions may
	 * thrown due to mismatches between persistency scheme and physical during
	 * query execution. Use CREATE the scheme changes. (Warning: loss of data)
	 * 
	 * It has an overhead of CREATE TABLE IF NOT EXISTS query per table. 
	 */
	UPDATE,
	
	/**
	 * Assumes that the table is identical to the existing scheme and does NOT
	 * create tables, it uses existing ones.
	 * 
	 * If physical scheme is not up to date, various runtime exceptions may
	 * thrown due to mismatches between persistency scheme and physical during
	 * query execution.
	 * 
	 * Recommended to switch {@link SchemeCreationPolicy.UPDATE} for once.
	 */
	USE_EXISTING;
}
