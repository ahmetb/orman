package demo3;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Ticket extends Model<Ticket>{
	@Id
	public long id;
	
	public String seat;

	public Payment payment;
	
	@Override
	public String toString() {
		return  id + " " + seat + " " + ((payment == null) ? null : payment.amount);
	}
}
