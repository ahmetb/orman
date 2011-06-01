package demo7depencencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToOne;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Luggage extends Model<Luggage>{
	@PrimaryKey(autoIncrement=true)
	private int luggageId;
	
	private float weight = 0;
	
	@ManyToOne
	private Booking toTicket;

	public void setLuggageId(int luggageId) {
		this.luggageId = luggageId;
	}

	public int getLuggageId() {
		return luggageId;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}

	public void setToTicket(Booking toTicket) {
		this.toTicket = toTicket;
	}

	public Booking getToTicket() {
		return toTicket;
	}
}
