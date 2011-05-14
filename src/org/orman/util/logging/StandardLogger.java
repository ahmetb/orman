package org.orman.util.logging;

import java.util.Date;

public class StandardLogger implements ILogger {

	public void trace(Object message){
		log("TRACE", message);
	}
	
	public void trace(String message, Object... params){
		log("TRACE", String.format(message, params));
	}
	
	public void debug(Object message){
		log("DEBUG", message);
	}
	
	public void debug(String message, Object... params){
		log("DEBUG", String.format(message, params));
	}
	
	public void info(Object message){
		log("INFO", message);
	}
	
	public void info(String message, Object... params){
		log("INFO", String.format(message, params));
	}
	
	public void warn(Object message){
		log("WARN", message);
	}
	
	public void warn(String message, Object... params){
		log("WARN", String.format(message, params));
	}
	
	public void error(Object message){
		logErr("ERROR", message);
	}
	
	public void error(String message, Object... params){
		logErr("ERROR", String.format(message, params));
	}
	
	public void fatal(Object message){
		logErr("FATAL", message);
	}
	
	public void fatal(String message, Object... params){
		logErr("FATAL", String.format(message, params));
	}
	
	
	private void log(String level, Object message){
		System.out.println(new Date().toString() + " [" + level + "] " + message);
	}
	
	private void logErr(String level, Object message){
		System.err.println(new Date().toString() + " [" + level + "] " + message);
	}

	@Override
	public void setLevel(LoggingLevel level) {
		// TODO currently ignore all levels. level filtering needed.
	}
}
