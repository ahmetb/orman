package demo;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToMany;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;

@Entity
public class ClassRoom extends Model<ClassRoom> {
	@Id
	public int id;
	
	public String name;
	
	@OneToMany(on = "classroom", toType = Student.class)
	public List<Student> students;
	
	@Override
	public String toString() {
		return "-CLASSROOM  " + id+" "+name;
	}
	
	public static void main(String[] args) {
		Log.setLevel(LoggingLevel.TRACE);
		
		Database db = new SQLite("lite.db");
		MappingSession.registerDatabase(db);
		
		MappingSession.registerPackage("demo");
		
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.start();
		
		ClassRoom c = new ClassRoom();
		c.name = "1a";
		

		c.insert();
		
		Student s1 = new Student();
		s1.name = "abc";
		s1.classroom = c;
		
		Student s2 = new Student();
		s2.name = "def";
		s2.classroom = c;
		
		Student s3 = new Student();
		s3.name = "ghi";
		s3.classroom = c;
		
		s1.insert();
		s2.insert();
		s3.insert();
		
	}
}
