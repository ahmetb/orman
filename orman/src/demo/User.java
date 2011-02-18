package demo;

import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;
import org.orman.sql.Query;

@Entity(table="user")
public class User extends Model<User> {
	@Id public String id;
	
	@OneToOne
	public Notebook bookOfUser; 

	public User(){
	}

	public static void main(String[] args) {
		MappingSession.registerEntity(User.class);
		MappingSession.registerEntity(Notebook.class);
		MappingSession.start();
		
		User u =new User();
		Notebook n = new Notebook();
		n.insert();
		u.bookOfUser = n;
		u.insert();
		u.delete();
		
		Query q = ModelQuery.select().from(User.class).where(
				C.eq(User.class, "bookOfUser", n) // extract id of entity  
				).getQuery();
		
//		System.out.println(q);
		
			
	}
}
