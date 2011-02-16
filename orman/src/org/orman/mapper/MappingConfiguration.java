package org.orman.mapper;

public class MappingConfiguration {
	
	private PhysicalNamingPolicy columnNamePolicy  = new PhysicalNamingPolicy().uppercase(false).pluralize(false).underscore(true);
	private PhysicalNamingPolicy tableNamePolicy  = new PhysicalNamingPolicy().uppercase(false).pluralize(true).underscore(true);

	private SchemeCreationPolicy creationPolicy = SchemeCreationPolicy.CREATE;

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

	public void setCreationPolicy(SchemeCreationPolicy creationPolicy) {
		this.creationPolicy = creationPolicy;
	}

	public SchemeCreationPolicy getCreationPolicy() {
		return creationPolicy;
	}
}
