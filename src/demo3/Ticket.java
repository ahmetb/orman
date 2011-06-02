package demo3;

import java.util.Date;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.sql.IndexType;

@Entity
public class Ticket extends Model<Ticket>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String seat;
	
	public TicketType type = TicketType.ECONOMY;
	
	@Index(type=IndexType.BTREE)
	public Payment payment;
	
	public Date date;
	
	@Override
	public String toString() {
		return  "ID:"+id + " (" + date + ") " + seat + " "+type+" " + ((payment == null) ? null : payment.amount)+"\n";
	}
}
