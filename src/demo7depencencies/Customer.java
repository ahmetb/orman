package demo7depencencies;

import org.orman.mapper.EntityList;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Customer {
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	public String name;
	
	public String phone;
	
	public int age;
	
	public City city;
	
	@OneToMany(onField = "customer", toType = Booking.class)
	public EntityList<Customer, Booking> bookings = new EntityList<Customer, Booking>(Customer.class, Booking.class, this);
	
	
}
