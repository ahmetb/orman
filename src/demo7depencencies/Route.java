package demo7depencencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Route extends Model<Route>{
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	public Airport departure;
	
	public Airport destination;
	
}
