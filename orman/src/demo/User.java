package demo;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.NotNull;
import org.orman.sql.Query;
import org.orman.sqlite.SQLite;

@Entity(table="user")
public class User extends Model<User> {
	@Id public int id;
	public String name;
	
	@NotNull
	public int bookOfUser;
	
	@Override
	public String toString() {
		return id+"-"+name;
	}

	public static void main(String[] args) {
		Database db = new SQLite("lite.db");
		
		MappingSession.registerDatabase(db);
		MappingSession.registerEntity(User.class);
		MappingSession.registerEntity(Notebook.class);
		MappingSession.getConfiguration().setCreationPolicy(SchemeCreationPolicy.USE_EXISTING);
		MappingSession.start();
		
//		User u = new User();
//		Notebook n = new Notebook();
//		n.insert();
//		u.bookOfUser = n;
//		u.insert();
//		n.whoseIsThat = u;
//		n.update();
//		System.out.println(u.countAll());
		
		Query custom = ModelQuery.select().from(User.class).where(C.gt(User.class, "id", 0)).getQuery();
		List<User> l = Model.fetchQuery(custom, User.class);
		for(User u : l)
			System.out.println(u);
	}
}
