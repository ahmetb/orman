package demo8dependencies;


import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.ILogger;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;
import org.orman.util.logging.StandardLogger;

public class Main {
	public static void main(String[] args) {
		Database db = new SQLite("deps.db~");
		ILogger log = new StandardLogger();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.TRACE);
		
		MappingSession.registerDatabase(db);
		MappingSession.start();
	}
}
