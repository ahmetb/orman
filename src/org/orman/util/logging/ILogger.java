package org.orman.util.logging;

public interface ILogger {
	public void setLevel(LoggingLevel level);
	
	public void trace(Object message);
	
	public void trace(String message, Object... params);
	
	public void debug(Object message);
	
	public void debug(String message, Object... params);
	
	public void info(Object message);
	
	public void info(String message, Object... params);
	
	public void warn(Object message);
	
	public void warn(String message, Object... params);
	
	public void error(Object message);
	
	public void error(String message, Object... params);
	
	public void fatal(Object message);
	
	public void fatal(String message, Object... params);
}
