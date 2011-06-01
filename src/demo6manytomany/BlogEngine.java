package demo6manytomany;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;
import org.orman.util.logging.StandardLogger;

public class BlogEngine {
	public static void main(String[] args) {
		Database db = new SQLite("blog.db~");
		//Database db = new MySQL(new MySQLSettingsImpl("root", "root", "test"));
		StandardLogger log = new StandardLogger();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.TRACE);
		
		//disable auto entity registration
		MappingSession.setAutoPackageRegistration(false);
		
		//Manual entity register
		MappingSession.registerEntity(BlogPost.class);
		MappingSession.registerEntity(Keyword.class);
		
		MappingSession.registerDatabase(db);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.start();
		
		Keyword k1 = new Keyword("general"); k1.insert();
		Keyword k2 = new Keyword("linux"); k2.insert();
		Keyword k3 = new Keyword("amd"); k3.insert();
		Keyword k4 = new Keyword("intel"); k4.insert();
		System.out.println(""+k1 +'\n' + k2+'\n'  + k3+'\n'  + k4);
		
		BlogPost p1 = new BlogPost();
		p1.title = "Linux on AMD";
		p1.insert();
		
		BlogPost p2 = new BlogPost();
		p2.title = "Linux on Intel";
		p2.insert();
		
		// for p1, add keywords on post.
		p1.keywords.add(k1);
		p1.keywords.add(k2);
		p1.keywords.add(k3);
		
		// for p2, add keywords on keyword using post.
		k1.posts.add(p2);
		k2.posts.add(p2);
		k4.posts.add(p2);
		
		
		System.out.println("\n\nRequery posts.");
		List<BlogPost> posts = Model.fetchAll(BlogPost.class);
		for(BlogPost p : posts){
			System.out.println("********");
			System.out.println("** Post : " + p.title);
			System.out.println("** Keywords : " + p.keywords);
		}
		
		System.out.println("\n\nRequery keywords");
		List<Keyword> kws = Model.fetchAll(Keyword.class);
		for(Keyword k : kws){
			System.out.println("********");
			System.out.println("** Keyword : " + k.word);
			System.out.println("** Containing posts : " + k.posts);
		}
	}
}
