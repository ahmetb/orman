package demo2;

import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mysql.MySQL;
import org.orman.mysql.MySQLSettingsImpl;

public class Main {
	public static void main(String[] args) {
		MySQLSettingsImpl settings = new MySQLSettingsImpl("root", "root", "test");
		Database db = new MySQL(settings);
		
		MappingSession.registerDatabase(db);
		MappingSession.registerEntity(Account.class);
		MappingSession.registerEntity(Customer.class);
		MappingSession.registerEntity(Owns.class);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		
		MappingSession.start();
		
		
	}
}
