package demo3;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;

@Entity
public class Payment extends Model<Payment>{
	@Id
	public long tx_id;
	
	public float amount;
	
	@Override
	public String toString() {
		return tx_id+ " $"+  amount;
	}
}
