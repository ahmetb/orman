package demo;

import java.io.BufferedReader;

import org.orman.mapper.C;
import org.orman.mapper.F;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.Index;
import org.orman.sql.QueryType;

@Entity(table="user")
public class User extends Model<User> {
	@Id public int id;
	int age;
	
	@Index private String lastName;
	public boolean isAdmin;
	private transient int tmp;
	private transient BufferedReader reader;
	
	public User(){
		age = 5;
	}
	
	public static void main(String[] args) {
		MappingSession.registerEntity(User.class);
		MappingSession.start();
		
		User u = new User();
		u.insert();
		u.id=5;
		u.setLastName("zax");
		u.update();
		u.id=6;
		u.update();
		u.delete();
		
		ModelQuery q = ModelQuery.type(QueryType.SELECT);
		q.from(User.class).orderBy("-User.id");
		
		System.out.println(q.getQuery());
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
}
