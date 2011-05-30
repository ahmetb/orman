package demo6manytomany;

import org.orman.mapper.EntityList;
import org.orman.mapper.LoadingPolicy;
import org.orman.mapper.Model;
import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.ManyToMany;
import org.orman.mapper.annotation.PrimaryKey;

@Entity
public class Keyword extends Model<Keyword>{
	@PrimaryKey(autoIncrement=true)
	public long id;
	
	public String word;
	
	@ManyToMany(toType = BlogPost.class, load=LoadingPolicy.LAZY)
	public EntityList<Keyword, BlogPost> posts = new EntityList<Keyword, BlogPost>(Keyword.class, BlogPost.class, this);
	
	public Keyword(){}
	
	public Keyword(String w){
		word = w;
	}
	
	@Override
	public String toString() {
		return "KW#"+id + " " + word;
	}
}
