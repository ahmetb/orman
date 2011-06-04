package org.orman.mapper;

/**
 * Holds mapping configuration policies.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 */
public class MappingConfiguration {
	
	// naming conventions
	private PhysicalNamingPolicy columnNamePolicy = new PhysicalNamingPolicy()
			.uppercase(false).pluralize(false).underscore(true);
	private PhysicalNamingPolicy tableNamePolicy = new PhysicalNamingPolicy()
			.uppercase(false).pluralize(false).underscore(true);
	private PhysicalNamingPolicy indexNamePolicy = new PhysicalNamingPolicy()
			.uppercase(false).pluralize(false).underscore(true);

	// default policies
	private SchemeCreationPolicy creationPolicy = SchemeCreationPolicy.CREATE_IF_NOT_EXISTS;
	private IdGenerationPolicy idGenerationPolicy = IdGenerationPolicy.DEFER_TO_DBMS;

	public IdGenerationPolicy getIdGenerationPolicy() {
		return idGenerationPolicy;
	}

	public void setIdGenerationPolicy(IdGenerationPolicy idGenerationPolicy) {
		this.idGenerationPolicy = idGenerationPolicy;
	}

	public PhysicalNamingPolicy getColumnNamePolicy() {
		return columnNamePolicy;
	}

	public void setColumnNamePolicy(PhysicalNamingPolicy columnNamePolicy) {
		this.columnNamePolicy = columnNamePolicy;
	}

	public PhysicalNamingPolicy getTableNamePolicy() {
		return tableNamePolicy;
	}

	public void setTableNamePolicy(PhysicalNamingPolicy tableNamePolicy) {
		this.tableNamePolicy = tableNamePolicy;
	}
	
	/**
	 * Sets naming policy for table and column name generation.
	 */
	public void setNamePolicy(PhysicalNamingPolicy policy){
		setColumnNamePolicy(policy);
		setTableNamePolicy(policy);
		setIndexNamePolicy(policy);
	}

	public void setCreationPolicy(SchemeCreationPolicy creationPolicy) {
		this.creationPolicy = creationPolicy;
	}

	public SchemeCreationPolicy getCreationPolicy() {
		return creationPolicy;
	}

	public void setIndexNamePolicy(PhysicalNamingPolicy indexNamePolicy) {
		this.indexNamePolicy = indexNamePolicy;
	}

	public PhysicalNamingPolicy getIndexNamePolicy() {
		return indexNamePolicy;
	}
}
