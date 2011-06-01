package demo7depencencies;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class GroundStaff extends Model<GroundStaff>{
	@PrimaryKey
	public String ssn;
	
	public Airport office;
}
