package org.orman.sql;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orman.sql.exception.DuplicateSubclauseException;
import org.orman.sql.util.Serializer;

public class Query extends DataSource implements Aliasable {
	private QueryType type; // in tostring
	private List<Table> tables;
	private String database;
	private List<IQueryField> fieldList;
	private Map<String, String> valuedFieldMap; 
	private Map<SubclauseType, ISubclause> subclauses;
	private List<TableConstraint> constraints;


	private String indexName;

	private String alias;

	public Query() {
		tables = new ArrayList<Table>();
		subclauses = new EnumMap<SubclauseType, ISubclause>(SubclauseType.class);
		fieldList = new ArrayList<IQueryField>();
		valuedFieldMap = new HashMap<String, String>();
		constraints = new ArrayList<TableConstraint>();
	}

	public Query(QueryType queryType) {
		this();
		this.type = queryType;
	}

	public Query as(String alias) {
		this.alias = alias;
		return this;
	}

	public Query addTable(String tableName) {
		Table newTable = new Table(tableName);
		tables.add(newTable);
		return this;
	}
	
	public Query addTable(String tableName, String as) {
		if ("".equals(as)) as = null;
		
		Table newTable = new Table(tableName, as);
		newTable.setHandle(as);
		tables.add(newTable);
		return this;
	}


	@Override
	public String getAlias() {
		return this.alias;
	}

	public void addField(IQueryField field) {
		fieldList.add(field);
	}

	public List<IQueryField> getFieldList() {
		return fieldList;
	}
	
	public void addSubclause(SubclauseType t, ISubclause s) {
		if(this.subclauses.get(t) != null)
			throw new DuplicateSubclauseException(t.toString());
		this.subclauses.put(t, s);
	}
	
	public void addTableConstraint(TableConstraint t) {
		constraints.add(t);
	}

	public ISubclause getSubclause(SubclauseType t) {
		return this.subclauses.get(t);
	}

	public QueryType getType() {
		return type;
	}

	public List<Table> getTables() {
		return tables;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	public void setField(String field, Object value){
		String literal = Serializer.serialize(value); 
		valuedFieldMap.put(field, literal);
	}

	public Map<String, String> getValuedFieldMap() {
		return valuedFieldMap;
	}
	
	public List<TableConstraint> getConstraints() {
		return constraints;
	}
	
	/**
	 * Returns the query representation by building a new QueryBuilder
	 * and passing query to it.
	 */
	@Override
	public String toString(){
		return QueryBuilder.getBuilder(this).prepareSql();
	}
	
	/**
	 * Get executable query (preceeded by semicolon)
	 */
	public String getExecutableSql(){
		return new StringBuffer(this.toString()).append(';').toString();
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}
}
