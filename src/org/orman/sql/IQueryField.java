package org.orman.sql;

public interface IQueryField extends Aliasable {
	public String getFieldRepresentation();
	public String getFieldName();
	public String getAlias();
}