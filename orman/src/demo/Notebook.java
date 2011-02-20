package demo;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Notebook extends Model<Notebook>{
	@Id public long id;
	public String name;
	
	@OneToOne
	public User whose;
	
	@Override
	public String toString() {
		return "Notebook with id="+id+" named="+name +" of user="+((whose == null) ? "?" : "@"+whose.id);
	}
}
