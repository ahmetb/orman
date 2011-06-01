package demo7depencencies;

import java.util.Date;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Flight extends Model<Flight>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public Date departure;
	
	public float fare = 0;
	
	public Route route;
	
	@ManyToMany(toType = AirStaff.class)
	public EntityList<Flight, AirStaff> crew = new EntityList<Flight, AirStaff>(Flight.class, AirStaff.class, this);
}
