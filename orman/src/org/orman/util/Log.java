package org.orman.util;

import java.text.MessageFormat;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Log {
	private static Logger logger = Logger.getLogger("orman");
	static {
		BasicConfigurator.configure();
	}
	
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger log) {
		logger = log;
	}
	
	
	
	public static void trace(Object message){
		getLogger().trace(message);
	}
	
	public static void trace(String message, Object... params){
		getLogger().debug(String.format(message, params));
	}
	
	public static void debug(Object message){
		getLogger().debug(message);
	}
	
	public static void debug(String message, Object... params){
		getLogger().debug(String.format(message, params));
	}
	
	public static void info(Object message){
		getLogger().info(message);
	}
	
	public static void info(String message, Object... params){
		getLogger().info(String.format(message, params));
	}
	
	public static void warn(Object message){
		getLogger().warn(message);
	}
	
	public static void warn(String message, Object... params){
		getLogger().warn(String.format(message, params));
	}
	
	public static void error(Object message){
		getLogger().error(message);
	}
	
	public static void error(String message, Object... params){
		getLogger().error(String.format(message, params));
	}
	
	public static void fatal(Object message){
		getLogger().fatal(message);
	}
	
	public static void fatal(String message, Object... params){
		getLogger().fatal(String.format(message, params));
	}
}
