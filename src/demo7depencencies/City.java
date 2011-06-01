package demo7depencencies;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class City extends Model<City>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String name;
	
	@OneToMany(onField = "city", toType = Airport.class)
	public EntityList<City,Airport> airports = new  EntityList<City, Airport>(City.class , Airport.class, this);
}
