package demo3;

import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.AutoIncrement;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Ticket extends Model<Ticket>{
	@AutoIncrement
	public long id;
	
	public String seat;
	
	@OneToOne(load=LoadingPolicy.LAZY)
	public Payment payment;
	
	@Override
	public String toString() {
		return  id + " " + seat + " " + ((payment == null) ? null : payment.amount);
	}
}
