package demo3;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Payment extends Model<Payment>{
	@PrimaryKey(autoIncrement=true)
	public int tx_id;
	
	public float amount;
	
	@Override
	public String toString() {
		return tx_id+ " $"+  amount;
	}
}
