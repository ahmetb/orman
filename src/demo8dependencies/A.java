package demo8dependencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity 
public class A extends Model<A>{
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	public D d;
}
