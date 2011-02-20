package org.orman.datasource;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores rows as array of column values which is column values
 * are Object[][]. It also stores column names (if available).
 * 
 * Those Objects may be {@link String}, {@link Float}, {@link Double},
 * {@link Integer} or <code>null</code>. Be cautions while using.
 * 
 * @author alp
 */
public class ResultList {
	private String[] columnNames;
	private Map<String, Integer> columnNameMap;
	
	private Object[][] records;
	
	/**
	 * Initializes columnNames with given record array.
	 * @param columnNames
	 * @param records first dimension is the row, second dimension is the column.
	 */
	public ResultList(String[] columnNames, Object[][] records){
		this.setColumnNames(columnNames);
		this.records = records;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
		
		// put (colName, colIndex) to the map.
		if(this.columnNames != null){
			columnNameMap = new HashMap<String, Integer>();
			for(int i = 0; i < columnNames.length; i++){
				columnNameMap.put(columnNames[i], i);
			}
		}
	}

	public String[] getColumnNames() {
		return columnNames;
	}
	
	/**
	 * Returns column value of a row which is at given index.
	 * @param index starts from 0.
	 * @param column column name. throws error if does not exist. case-sensitive.
	 * @return
	 */
	public Object getColumn(int rowIndex, String column){
		return this.records[rowIndex][columnNameMap.get(column)];
	}
	
	public Object[] getRow(int rowIndex){
		return this.records[rowIndex];
	}
	
	public int getColumnCount(){
		return this.columnNames == null ? 0 : this.columnNames.length;
	}
	
	public int getRecordCount(){
		return this.records == null ? 0 : this.records.length;
	}
	
	/**
	 * Nice representation of result list.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("--------RESULT LIST---------\n");
		sb.append("Columns: ");
		for(int i = 0; i < getColumnCount(); i++)
			sb.append(getColumnNames()[i]+" ");
		sb.append("\nNumber of rows: ");
		sb.append(getRecordCount());
		sb.append('\n');
		for(int i = 0; i < getRecordCount(); i++){
			Object[] row = getRow(i);
			for(int j = 0; j < row.length; j++){
				sb.append(row[j]);
				if (j != row.length-1) sb.append(", ");
			}
			sb.append('\n');
		}
		sb.append("----------------------------\n");
		return sb.toString();
	}
}