package demo;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.OneToOne;
import org.orman.sql.Query;
import org.orman.sqlite.SQLite;

@Entity(table="user")
public class User extends Model<User> {
	@Id public int id;
	public String name;
	
	@OneToMany(on = "whose", toType = Notebook.class)
	public Notebook books;
	
	@Override
	public String toString() {
		return "User "+id+"'s books are {"+books+"}";
	}

	public static void main(String[] args) {
		Database db = new SQLite("lite.db");
		
		MappingSession.registerDatabase(db);
		MappingSession.registerEntity(User.class);
		MappingSession.registerEntity(Notebook.class);
		MappingSession.getConfiguration().setCreationPolicy(SchemeCreationPolicy.CREATE);
		MappingSession.start();
		
		User a = new User();
		a.insert();
		
		Notebook n = new Notebook();
		n.whose = a;
		n.insert();

		n.name = a.id+"s book";
		n.update();
		
		a.books=n;
		a.update();
		
//		Notebook m = new Notebook();
//		m.whose = a;
//		m.insert();
//		m.name = a.id+"s second book";
//		m.update();
		
		Query custom = ModelQuery.select().from(User.class).getQuery();
		List<User> l = Model.fetchQuery(custom, User.class);
		System.out.println(l);
		
		Query custom2 = ModelQuery.select().from(Notebook.class).getQuery();
		List<Notebook> z = Model.fetchQuery(custom2, Notebook.class);
		for(Notebook nb: z)
			System.out.println(nb.id+"+"+nb.whose);
	}
}
