package demo2;

import java.util.Date;
import java.util.Calendar;

import org.orman.datasource.Database;
import org.orman.mapper.IdGenerationPolicy;
import org.orman.mapper.MappingSession;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.mysql.MySQL;
import org.orman.mysql.MySQLSettingsImpl;

public class Main {
	public static void main(String[] args) {
		MySQLSettingsImpl settings = new MySQLSettingsImpl("root", "root", "test");
		Database db = new MySQL(settings);
		
		MappingSession.registerDatabase(db);
		MappingSession.registerEntity(Account.class);
		MappingSession.registerEntity(Customer.class);
		MappingSession.registerEntity(Owns.class);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.getConfiguration().setIdGenerationPolicy(
				IdGenerationPolicy.MANUAL);
		
		MappingSession.start();
		
		createData();
		
		db.closeConnection();
	}

	private static void createData() {
		Account a = new Account();
		a.setAid("A0000001");
		a.setBalance(2000f);
		a.setBranch("kizilay");
		a.setOpenDate(date(2005,1,1));
		a.insert();
		
		Account b = new Account();
		b.setAid("A0000002");
		b.setBalance(8000f);
		b.setBranch("bilkent");
		b.setOpenDate(date(2006,1,1));
		b.insert();
		
		Account c = new Account();
		c.setAid("A0000003");
		c.setBalance(4000f);
		c.setBranch("cankaya");
		c.setOpenDate(date(2007,1,1));
		c.insert();
		
		Account d = new Account();
		d.setAid("A0000004");
		d.setBalance(1000f);
		d.setBranch("sincan");
		d.setOpenDate(date(2008,1,1));
		d.insert();
		
		Account e = new Account();
		e.setAid("A0000005");
		e.setBalance(3000f);
		e.setBranch("tandogan");
		e.setOpenDate(date(2009,1,1));
		e.insert();
		
		Account f = new Account();
		f.setAid("A0000006");
		f.setBalance(5000f);
		f.setBranch("eryaman");
		f.setOpenDate(date(2009,1,1));
		f.insert();
		
		
		Customer c1 = new Customer();
		c1.setBirthDate(date(1980, 10, 10));
		c1.setAddress("Tunali");
		c1.setCity("ankara");
		c1.setName("Cem");
		c1.setCid("20000001");
		c1.insert();
		
		Customer c2 = new Customer();
		c2.setBirthDate(date(1985, 9, 8));
		c2.setAddress("Nisantasi");
		c2.setCity("izmir");
		c2.setName("Asli");
		c2.setCid("20000002");
		c2.insert();
		
		Owns o1 = new Owns(); o1.setAccount(a); o1.setCustomer(c1); o1.insert();
		Owns o2 = new Owns(); o2.setAccount(b); o2.setCustomer(c1); o2.insert();
		Owns o3 = new Owns(); o3.setAccount(c); o3.setCustomer(c1); o3.insert();
		Owns o4 = new Owns(); o4.setAccount(d); o4.setCustomer(c1); o4.insert();
		Owns o5 = new Owns(); o5.setAccount(b); o5.setCustomer(c2); o5.insert();
		Owns o6 = new Owns(); o6.setAccount(c); o6.setCustomer(c2); o6.insert();
		Owns o7 = new Owns(); o7.setAccount(e); o7.setCustomer(c2); o7.insert();
		Owns o8 = new Owns(); o8.setAccount(f); o8.setCustomer(c2); o8.insert();
	}
	
	private static Date date(int year, int month, int day){
		Calendar d = Calendar.getInstance();
		d.set(Calendar.YEAR, year);
		d.set(Calendar.MONTH, month-1);
		d.set(Calendar.DAY_OF_MONTH, day);
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		d.set(Calendar.MILLISECOND, 0);
		return d.getTime();
	}
}
