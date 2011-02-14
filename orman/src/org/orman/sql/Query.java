package org.orman.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.orman.sql.exception.DuplicateSubclauseException;
import org.orman.sql.util.Serializer;

public class Query extends DataSource implements Aliasable {
	private QueryType type; // in tostring
	private List<Table> tables;
	private String database;
	private List<IQueryField> fieldList;
	private Map<String, String> valuedFieldMap; 
	private Map<SubclauseType, ISubclause> subclauses;
	private String indexName;

	private String alias;

	private Set<String> tableAliases;

	public Query() {
		tables = new ArrayList<Table>();
		fieldList = new ArrayList<IQueryField>();
		valuedFieldMap = new HashMap<String, String>();
		subclauses = new HashMap<SubclauseType, ISubclause>(); // TODO use
																// enummap

		tableAliases = new HashSet<String>();
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
		if (as == null || "".equals(as)) as = acquireTableAlias(tableName);
		
		System.out.println("handle = "+as);
		
		Table newTable = new Table(tableName, as);
		newTable.setHandle(as);
		tables.add(newTable);
		return this;
	}

	private String acquireTableAlias(String tableName) {
		String alias = null;
		int num = 0;
		do {
			// myTable->m0, m1, m2, m3, m4, m5
			alias = tableName.trim().toLowerCase().charAt(0) + "" + (num++);
		} while (tableAliases.contains(alias));

		return alias;
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
	
	/**
	 * Returns the query representation by building a new QueryBuilder
	 * and passing query to it.
	 */
	@Override
	public String toString(){
		return QueryBuilder.getBuilder(this).prepareSql();
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexName() {
		return indexName;
	}
}
