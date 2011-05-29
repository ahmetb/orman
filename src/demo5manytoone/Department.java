package demo5manytoone;

import org.orman.mapper.EntityList;
import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Department extends Model<Department>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String title;
	
	@OneToMany(toType = Employee.class, on = "dept", load=LoadingPolicy.LAZY)
	public EntityList<Department, Employee> employees;
	
	@Override
	public String toString() {
		return title;
	}
}
