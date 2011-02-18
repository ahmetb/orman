package demo;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Id;
import org.orman.mapper.annotation.OneToOne;

@Entity
public class Notebook extends Model<Notebook>{
	@Id public long id;
	@OneToOne public User whoseIsThat;
	
	public Notebook(){
	}
}
