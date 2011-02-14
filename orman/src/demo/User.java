package demo;

import java.io.BufferedReader;

import org.orman.mapper.annotation.Entity;
import org.orman.mapper.annotation.Index;

@Entity(table="user")
public class User {
	@Index(unique=true) private int id;
	private int age;
	private String lastName;
	private boolean isAdmin;
	private transient int tmp;
	private transient BufferedReader reader;
}
