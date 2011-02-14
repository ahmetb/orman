package demo;

import java.io.BufferedReader;

import org.orman.mapper.annotation.Entity;

@Entity(table="user")
public class User {
	private int id, IDx;
	private int age;
	private String lastName;
	private boolean isAdmin;
	private transient int tmp;
	private BufferedReader reader;
}
