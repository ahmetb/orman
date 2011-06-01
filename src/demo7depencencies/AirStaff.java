package demo7depencencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class AirStaff extends Model<AirStaff> {
	@PrimaryKey(autoIncrement=true)
	public int id;
	
	public String name;
	
	public String role;
}
