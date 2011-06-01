package demo8dependencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class D extends Model<D>{
	@PrimaryKey(autoIncrement=true)
	public int id;

}
