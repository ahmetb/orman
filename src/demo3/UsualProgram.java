package demo3;

import java.util.List;

import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.ModelQuery;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.ILogger;
import org.orman.util.logging.Log;
import org.orman.util.logging.Log4jAdapter;
import org.orman.util.logging.LoggingLevel;

public class UsualProgram {
	public static void main(String[] args) {
		SQLite db = new SQLite("lite.db");

		ILogger log = new Log4jAdapter();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.WARN);
		MappingSession.registerEntity(Payment.class);
		MappingSession.registerEntity(Ticket.class);

		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.UPDATE);
		MappingSession.registerDatabase(db);
		MappingSession.start();

		Payment p = new Payment();
		p.amount = (float) (Math.random() * 1000);
		p.insert();

		Ticket t = new Ticket();
		t.seat = "abc" + Math.random();
		t.payment = p;
		t.insert();

		List<Ticket> tickets = Model
				.fetchQuery(ModelQuery.select().from(Ticket.class).getQuery(),
						Ticket.class);
		System.out.println(tickets);
	}
}
