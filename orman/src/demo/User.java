package demo;

import org.orman.datasource.Database;
import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;
import org.orman.sql.Query;
import org.orman.sqlite.SQLite;

@Entity(table="user")
public class User extends Model<User> {
	@Id public String id;
	
	@OneToOne
	public Notebook bookOfUser; 

	public User(){
	}

	public static void main(String[] args) {
		Database db = new SQLite("lite.db");
		
		MappingSession.registerEntity(User.class);
		MappingSession.registerEntity(Notebook.class);
		MappingSession.registerDatabase(db);
		MappingSession.start();
		
		User u =new User();
		Notebook n = new Notebook();
		n.insert();
		u.bookOfUser = n;
		u.insert();
		u.delete();
		
	}
}
