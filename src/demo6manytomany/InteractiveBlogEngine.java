package demo6manytomany;

import java.util.List;
import java.util.Scanner;

import org.orman.datasource.Database;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;
import org.orman.util.logging.StandardLogger;

public class InteractiveBlogEngine {
	public static void main(String[] args) {
		
		Database db = new SQLite("blog.db~");
		StandardLogger log = new StandardLogger();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.WARN);
		MappingSession.registerEntity(BlogPost.class);
		MappingSession.registerEntity(Keyword.class);
		MappingSession.registerDatabase(db);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.UPDATE);
		MappingSession.start();
		
		Scanner scan = new Scanner(System.in);
		
		int option = 0;
		
		do{
			System.out.print("====Menu====\n1.Show posts\t");
			System.out.print("2.Show keywords\t3.New keyword\t4.New post\t0.Exit\nChoose option: ");
			option = scan.nextInt();
			
			if (option == 1){
				// show posts
				List<BlogPost> posts = Model.fetchAll(BlogPost.class);
				for(BlogPost p : posts){
					System.out.println("* " + p.title);
					System.out.print("  - ");
					for(Keyword k : p.keywords){
						System.out.print(k.word+ ", ");
					}
					System.out.println();
				}
				
			}
			
			if (option == 2){ // show keywords
				List<Keyword> kws = Model.fetchAll(Keyword.class);
				for(Keyword k : kws){
					System.out.println("* " + k.word + " ("+k.posts.size()+")");
				}
			}
			
			if (option == 3){ // new keyword
				System.out.print("Enter keyword and press enter: ");
				String word = null;
				do{
					word = scan.nextLine();
				} while(word == null || "".equals(word));
				
				Keyword k = new Keyword(word);
				k.insert();
				System.err.println("Keyword created.");
			}
			
			if (option == 4) { // new post.
				System.out.print("Enter post title and press enter: ");
				String title = null;
				do{
					title = scan.nextLine();
				} while(title == null || "".equals(title));
				
				BlogPost b = new BlogPost();
				b.title = title;
				b.insert();
				
				List<Keyword> kws = Model.fetchAll(Keyword.class);
				System.out.println("Categories:");
				
				for(int i = 0 ; i < kws.size(); i++){
					System.out.println(i + "-> " + kws.get(i).word);
				}
				int kwChoice = -1;
				do{
					System.out.print("Enter a category number, -1 to quit: ");
					kwChoice = scan.nextInt();
					if (kwChoice>=0 && kwChoice < kws.size()){
						b.keywords.add(kws.get(kwChoice));
					}
				} while(kwChoice >= kws.size() || kwChoice >= 0);
				
				
				System.err.println("Post created.");
			}
		} while(option != 0);
		System.err.println(">>> Program terminated");
		
	}
}
