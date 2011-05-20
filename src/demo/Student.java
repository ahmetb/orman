package demo;

import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.ManyToOne;

@Entity
public class Student extends Model<Student>{
	@Id public long id;
	
	public String name;
	
	@ManyToOne(load=LoadingPolicy.LAZY)
	public ClassRoom classroom;
	
	@Override
	public String toString() {
		return "STD id=`"+id+"` named=`"+name +"` class="+((classroom == null) ? "?  " : "@"+classroom.id);
	}
}
