package demo;

import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Notebook extends Model<Notebook>{
	@Id public long id;
	public String name;
	
	@ManyToOne(load=LoadingPolicy.LAZY)
	public User whose;
	
	@Override
	public String toString() {
		return "NB id=`"+id+"` named=`"+name +"` user="+((whose == null) ? "?" : "@"+whose.id);
	}
}
