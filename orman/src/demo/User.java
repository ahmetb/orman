package demo;

import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.NotNull;
import org.orman.mapper.annotation.OneToOne;
import org.orman.sql.Query;

@Entity(table="user")
public class User extends Model<User> {
	@Id public String id;
	@NotNull private String lastName;
	
	@OneToOne
	public Notebook bookOfUser; 
	
	public boolean isAdmin;
	
	public User(){
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public static void main(String[] args) {
		MappingSession.registerEntity(User.class);
		MappingSession.registerEntity(Notebook.class);
		MappingSession.start();
		
		
		User u =new User();
		Notebook n = new Notebook();
		n.insert();
		u.setLastName("balkan");
		u.bookOfUser = n;
		u.insert();
		
		Query q = ModelQuery.select().from(User.class).where(
				C.eq(User.class, "bookOfUser", n) // extract id of entity  
				).getQuery();
		
		System.out.println(q);
		
			
	}
}
