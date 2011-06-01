package demo5manytoone;

import java.util.List;

import org.orman.datasource.Database;
import org.orman.mapper.EntityList;
import org.orman.mapper.MappingSession;
import org.orman.mapper.Model;
import org.orman.mapper.SchemeCreationPolicy;
import org.orman.sqlite.SQLite;
import org.orman.util.logging.Log;
import org.orman.util.logging.LoggingLevel;
import org.orman.util.logging.StandardLogger;

public class CompanyEngine {
	public static void main(String[] args) {
		
		Database db = new SQLite("dep.db~");
		StandardLogger log = new StandardLogger();
		Log.setLogger(log);
		Log.setLevel(LoggingLevel.TRACE);
		MappingSession.registerDatabase(db);
		MappingSession.getConfiguration().setCreationPolicy(
				SchemeCreationPolicy.CREATE);
		MappingSession.start();
		
		Department d1 = new Department();
		d1.title = "Incubation";
		
		Department d2 = new Department();
		d2.title = "Terminal";
		
		d1.insert();
		d2.insert();
		
		d1.employees = new EntityList(Department.class, Employee.class, d1);
		
		Employee e1 = new Employee();
		e1.name = "Ahmet";
		e1.insert();
		d1.employees.add(e1);
		
		
		Employee e2 = new Employee();
		e2.name = "Onur";
		e2.insert();
		e2.dept = d2;
		e2.update();
		
		d2.employees = new EntityList(Department.class, Employee.class, d2);
		
		Employee e3 = new Employee();
		e3.name = "Kubra";
		e3.insert();
		d2.employees.add(e3);
		
		System.out.println("Printing existing departments.");
		
		System.out.println(d1.employees);
		System.out.println(d2.employees);
		
		System.out.println("Requerying departments.");
		
		List<Department> depts = Model.fetchAll(Department.class);
		for(Department d : depts){
			System.out.println("**** Dept. " + d.toString() + "  " + d.employees);
		}
		
		System.out.println("End of program.");
		
	}

}
