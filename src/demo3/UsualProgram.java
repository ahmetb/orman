package demo3;

import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;

public class UsualProgram {
	public static void main(String[] args) {
		SQLite db = new SQLite("lite.db");
		MappingSession.registerEntity(Payment.class);
		MappingSession.registerEntity(Ticket.class);
        
		MappingSession.getConfiguration().setCreationPolicy(SchemeCreationPolicy.UPDATE);
        MappingSession.registerDatabase(db);
        MappingSession.start();
        
        
        Payment p = new Payment();
        p.amount = (float) (Math.random()*1000);
        p.insert();

        
        Ticket t = new Ticket();
        t.seat = "abc"+Math.random();
        t.payment = p;
        t.insert();
        
  
	}
}
