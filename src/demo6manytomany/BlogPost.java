package demo6manytomany;

import org.orman.mapper.EntityList;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;
import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.annotation.PrimaryKey;
import org.orman.sql.IndexType;

@Entity
public class BlogPost extends Model<BlogPost>{
	@PrimaryKey(autoIncrement=true)
	public long pid;
	
	@Index(type=IndexType.BTREE, name="title_index")
	public String title;
	
	@ManyToMany(toType = Keyword.class)
	public EntityList<BlogPost, Keyword> keywords = new EntityList<BlogPost, Keyword>(BlogPost.class, Keyword.class, this);
	
	@Override
	public String toString() {
		return "P#"+pid+ " "+title;
	}
}
