package org.orman.mysql;

import java.util.logging.Logger;

import org.orman.mapper.QueryExecutionContainer;
import org.orman.sql.Query;

public class QueryExecutionContainerImpl implements QueryExecutionContainer {

	@Override
	public void execute(Query q) {
		Logger.getLogger("").info(q.toString());
	}

}
