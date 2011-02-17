package demo;

import java.io.BufferedReader;

import org.orman.mapper.C;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.NotNull;
import org.orman.sql.QueryType;

@Entity(table="user")
public class User extends Model<User> {
	@Id public int id;
	int age;
	
	@NotNull private String lastName;
	public boolean isAdmin;
	private transient int tmp;
	private transient BufferedReader reader;
	
	public User(){
		age = 5;
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public int getTmp() {
		return tmp;
	}

	public void setTmp(int tmp) {
		this.tmp = tmp;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	public static void main(String[] args) {
		MappingSession.registerEntity(User.class);
		MappingSession.start();
		
		User u =new User();
		u.setLastName("zaa");
		u.insert();
		u.setLastName("ax");
		u.update();
		
		System.out.println();
		
		ModelQuery q = ModelQuery.type(QueryType.SELECT);
		q.fromAs(User.class, "zaa").orderBy("-User.id").where(
				C.and(
					C.eq(User.class, "lastName", 10),
					C.between(User.class, "age", 3, "balkan")
				)
		);
		
		System.out.println(q.getQuery());
	}

}
