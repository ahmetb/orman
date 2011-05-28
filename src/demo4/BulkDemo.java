package demo4;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.BulkInsert;
import org.orman.mapper.IdGenerationPolicy;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mysql.MySQL;
import org.orman.mysql.MySQLSettingsImpl;
import org.orman.util.logging.ILogger;
import org.orman.util.logging.Log;
import org.orman.util.logging.Log4jAdapter;
import org.orman.util.logging.LoggingLevel;

public class BulkDemo {
	public static void main(String[] args) {
		BulkInsert bulkInsertion = null;
		int totalRecord;
		
		ILogger log = new Log4jAdapter();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.TRACE);
		
		MySQLSettingsImpl settings = new MySQLSettingsImpl("root", "root", "test");
		Database db = new MySQL(settings);
		
		MappingSession.registerDatabase(db);
		
		MappingSession.registerPackage("demo4");
		
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.getConfiguration().setIdGenerationPolicy(
				IdGenerationPolicy.MANUAL);
		
		MappingSession.start();
		
		try {
			bulkInsertion = new BulkInsert(Product.class, "./src/demo4/products.txt","**",BulkInsert.NEW_LINE);
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
		
		totalRecord = bulkInsertion.startBulkInsert();
		
		if (totalRecord == -1) 
			System.out.println("Ops. Something went wrong!");
		else
			System.out.println(String.format("Total %d record(s) affected",totalRecord));
		

		List<Product> products = Model.fetchQuery(
				ModelQuery.select().from(Product.class).orderBy("-Product.id")
						.getQuery(), Product.class);
		
		System.out.println(products);
		
		db.closeConnection();
	}

}
