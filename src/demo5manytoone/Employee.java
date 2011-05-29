package demo5manytoone;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.sql.IndexType;

@Entity
public class Employee extends Model<Employee>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	@Index(type=IndexType.HASH)
	public String name;
	
	@ManyToOne
	public Department dept;
	
	@Override
	public String toString() {
		return id + " " + name + " @" + dept;
	}
}
