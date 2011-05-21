package org.orman.util.logging;

/**
 * A logger adapter interface which can be used by {@link Log}
 * so that it will provide a unified way to use any adapter
 * without any dependencies required.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 */
public interface ILogger {
	public void setLevel(LoggingLevel level);
	
	public void trace(String message, Object... params);
	
	public void debug(String message, Object... params);
	
	public void info(String message, Object... params);
	
	public void warn(String message, Object... params);
	
	public void error(String message, Object... params);
	
	public void fatal(String message, Object... params);
}
