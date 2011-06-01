package demo7depencencies;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.OneToOne;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Booking extends Model<Booking>{
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	@Index
	public Flight flight;
	
	@OneToOne
	public Payment payment;
	
	@ManyToOne
	public Customer owner;
	
	@OneToMany(onField = "toTicket", toType = Luggage.class)
	public EntityList<Booking, Luggage> luggage = new EntityList<Booking, Luggage>(Booking.class, Luggage.class, this);
}
