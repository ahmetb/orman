package demo3;

import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.OneToOne;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Ticket extends Model<Ticket>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String seat;
	
	public Payment payment;
	
	@Override
	public String toString() {
		return  id + " " + seat + " " + ((payment == null) ? null : payment.amount)+"\n";
	}
}
