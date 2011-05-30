package demo6manytomany;

import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Keyword extends Model<Keyword>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String word;
}
