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
import org.orman.sql.QueryBuilder;
import org.orman.sql.QueryType;

@Entity(table="user")
public class User extends Model<User> {
	@Id public int id;
	@NotNull private String lastName;
	
	@OneToOne
	public Notebook book; 
	
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
		u.setLastName("zaa");
		u.insert();
		
		System.out.println();
		
		ModelQuery q = ModelQuery.type(QueryType.SELECT);
			q.from(User.class)
			.where(C.eq(User.class, "lastName", 10))
			.orderBy("-User.id")
			.groupBy("User.lastName")
			.limit(10)
			.getQuery();
		
		System.out.println(q.getQuery());
		
		
		Query a = QueryBuilder.getBuilder(QueryType.CREATE_VIEW)
			.viewDetails(
					"myView", q.getQuery())
			.getQuery();
		
		System.out.println(a);
	}
}
