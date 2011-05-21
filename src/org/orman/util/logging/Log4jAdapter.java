package org.orman.util.logging;

import java.util.EnumMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Adapter for log4j logging library of Apache Software Foundation.
 * Default logging level is inherited from default {@link Logger}.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 *
 */
public class Log4jAdapter implements ILogger {
	
	private static final String FRAMEWORK_LOGGER_NAME = "orman";

	// Logging level mappings.
	@SuppressWarnings("serial")
	private static EnumMap<LoggingLevel, Level> correspondingLevel = new EnumMap<LoggingLevel, Level>(
			LoggingLevel.class) {
		{
			put(LoggingLevel.TRACE, Level.TRACE);
			put(LoggingLevel.DEBUG, Level.DEBUG);
			put(LoggingLevel.INFO, Level.INFO);
			put(LoggingLevel.WARN, Level.WARN);
			put(LoggingLevel.ERROR, Level.ERROR);
			put(LoggingLevel.FATAL, Level.FATAL);
		}
	};

	private Logger logger;

	public Log4jAdapter() {
		BasicConfigurator.configure();
		Logger.getLogger(FRAMEWORK_LOGGER_NAME);
	}

	public void setLogger(Logger log) {
		logger = log;
	}

	public Logger getLogger() {
		return logger;
	}
	
	@Override
	public void setLevel(LoggingLevel level) {
		setLevel(correspondingLevel.get(level));
	}

	public void setLevel(Level level) {
		logger.setLevel(level);
	}

	/* Log4j methods */
	public void trace(String message, Object... params) {
		getLogger().debug(String.format(message, params));
	}

	public void debug(String message, Object... params) {
		getLogger().debug(String.format(message, params));
	}

	public void info(String message, Object... params) {
		getLogger().info(String.format(message, params));
	}

	public void warn(String message, Object... params) {
		getLogger().warn(String.format(message, params));
	}

	public void error(String message, Object... params) {
		getLogger().error(String.format(message, params));
	}

	public void fatal(String message, Object... params) {
		getLogger().fatal(String.format(message, params));
	}

}
