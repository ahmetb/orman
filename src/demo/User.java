package demo;

import java.util.List;

import org.apache.log4j.Level;
import org.orman.datasource.Database;
import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToMany;
import org.orman.sql.Query;
import org.orman.sqlite.SQLite;
import org.orman.util.Log;

@Entity
public class User extends Model<User> {
	@Id
	public int id;
	public String name;
	
	@Override
	public String toString() {
		return id+" "+name;
	}
	
	public static void main(String[] args) {
		Log.setLevel(Level.TRACE);
		
		Database db = new SQLite("lite.db");
		MappingSession.registerDatabase(db);
		MappingSession.registerEntity(User.class);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.USE_EXISTING);
		MappingSession.start();
	
		User a = new User();
		a.name = "Alp";
		a.insert();
		
		Query del = ModelQuery.delete().from(User.class).where(C.eq(User.class, "id", 1)).getQuery();
		Model.execute(del);

		Query custom = ModelQuery.select().from(User.class).getQuery();
		List<User> l = Model.fetchQuery(custom, User.class);
		System.out.println(l);
	}
}
