package demo;

import java.io.BufferedReader;

import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.Index;

@Entity(table="user")
public class User extends Model<User> {
	@Id public long id;
	int age;
	
	@Index private String lastName;
	public boolean isAdmin;
	private transient int tmp;
	private transient BufferedReader reader;
	
	public User(){
		age = 5;
		lastName = "balkan";
	}
	
	public static void main(String[] args) {
		
		MappingSession.registerEntity(User.class);
		MappingSession.start();
		
		User u = new User();
		u.setLastName("foo");
		u.insert();
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
