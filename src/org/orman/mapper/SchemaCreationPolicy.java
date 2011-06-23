package org.orman.mapper;

/**
 * Determines how the schemas are designated on the <code>start()</code> of
 * {@link MappingSession}.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
public enum SchemaCreationPolicy {
	/**
	 * Drops the table (with truncating its data) if exists and reconstructs the
	 * whole table from scratch each initialization process. WARNING: Loss of
	 * data.
	 * 
	 * Required on changes in the schema.
	 */
	CREATE,

	/**
	 * Creates the tables ONLY if they do not exist. If they exists, it uses the
	 * existing database schema. Does not DROP existing tables and their data.
	 * 
	 * If physical schema is not up to date, various runtime exceptions may
	 * thrown due to mismatches between persistency schema and physical during
	 * query execution. Use CREATE the schema changes. (Warning: loss of data)
	 * 
	 * It has an overhead of CREATE TABLE IF NOT EXISTS query per table.
	 */
	CREATE_IF_NOT_EXISTS,

	/**
	 * Assumes that the table is identical to the existing schema and does NOT
	 * create tables, it uses existing ones.
	 * 
	 * If physical schema is not up to date, various runtime exceptions may
	 * thrown due to mismatches between persistency schema and physical during
	 * query execution.
	 * 
	 * Recommended to switch {@link SchemaCreationPolicy.CREATE_IF_NOT_EXISTS}
	 * for once.
	 */
	USE_EXISTING;
}
