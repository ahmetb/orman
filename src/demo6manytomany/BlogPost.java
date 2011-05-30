package demo6manytomany;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class BlogPost extends Model<BlogPost>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String title;
	
	@ManyToMany(toType = Keyword.class)
	public EntityList<BlogPost, Keyword> keywords = new EntityList<BlogPost, Keyword>(BlogPost.class, Keyword.class, this);
}
