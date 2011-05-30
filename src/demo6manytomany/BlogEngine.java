package demo6manytomany;

import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;
import org.orman.util.logging.StandardLogger;

public class BlogEngine {
	public static void main(String[] args) {
		Database db = new SQLite("blog.db~");
		StandardLogger log = new StandardLogger();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.TRACE);
		MappingSession.registerEntity(BlogPost.class);
		MappingSession.registerEntity(Keyword.class);
		MappingSession.registerDatabase(db);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.start();
	}
}
