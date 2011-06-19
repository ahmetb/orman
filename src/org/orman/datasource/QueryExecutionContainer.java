package org.orman.datasource;

import org.orman.datasource.exception.QueryExecutionException;
import org.orman.sql.Query;

/**
 * Provides a unified query execution container such that every DBMS can return
 * requested type of {@link Query} results. Wraps the database implementation.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan@gmail.com>
 * 
 */
public interface QueryExecutionContainer extends ConnectionEstablisher {

	/**
	 * Just executes the given query without obtaining any results from result
	 * set.
	 * 
	 * @throws QueryExecutionException
	 *             if error occurs during execution
	 */
	public void executeOnly(Query q);

	/**
	 * Returns a {@link ResultList} holder including results obtained from a
	 * result. Postcondition: return value is not <code>null</code>.
	 * 
	 * @throws QueryExecutionException
	 *             if error occurs during execution
	 */
	public ResultList executeForResultList(Query q);

	/**
	 * Returns first column of first row in the result set of given query
	 * result.
	 * 
	 * @return <code>null</code> if 1st col of 1st row is null or no rows
	 *         returned at all.
	 * @throws QueryExecutionException
	 *             if error occurs during execution
	 */
	public Object executeForSingleValue(Query q);

	/**
	 * Returns (numeric auto-increment) id value of last inserted row from
	 * database. Used for reverse mapping purposes in framework internals.
	 * Highly dependent on DBMS and may not be implemented.
	 * 
	 * @throws QueryExecutionException
	 *             if error occurs during execution
	 */
	public Object getLastInsertId();

	/**
	 * Returns last insert id in given type. See <code>getLastInsertId()</code>
	 * method for more.
	 * 
	 * @param ofType
	 *            desired type of
	 * @return <code>null</code> if conversion to <code>ofType</code> cannot be
	 *         done.
	 * @throws QueryExecutionException
	 *             if error occurs during execution
	 */
	public <T> Object getLastInsertId(Class<T> ofType);
	
}
