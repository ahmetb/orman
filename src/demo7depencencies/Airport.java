package demo7depencencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Airport extends Model<Airport>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String name;
	
	public String icaoCode;
	
	public City city;
}
