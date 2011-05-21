package org.orman.util.logging;

/**
 * 
 * Logging class for ORMAN framework. Used for internal logging purposes. You
 * can set your own logger using <code>setLogger()</code> method, otherwise it
 * will use {@link StandardLogger} which is very primitive and outputs to the
 * stdout.
 * 
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 * 
 */
public class Log {
	private static ILogger logger;
	static {
		logger = new StandardLogger();
	}

	/**
	 * Set a customized logger for the framework internals. Logging level is set
	 * to default level of given {@link ILogger} adapter. Therefore, you should
	 * use <code>setLevel()</code> after setting a new logger.
	 * 
	 * @param loggerAdapter
	 */
	public static void setLogger(ILogger loggerAdapter) {
		logger = loggerAdapter;
	}

	private static ILogger getLogger() {
		return logger;
	}

	/**
	 * Set a customized threshold level for logger. Should be called again if
	 * logger is changed by <code>setLogger()</code>.
	 * 
	 * @param level
	 *            minimum level of logging messages to show.
	 */
	public static void setLevel(LoggingLevel level) {
		getLogger().setLevel(level);
	}

	/* Public static dispatcher methods. */
	public static void trace(String message, Object... params) {
		getLogger().trace(String.format(message, params));
	}

	public static void debug(String message, Object... params) {
		getLogger().debug(String.format(message, params));
	}

	public static void info(String message, Object... params) {
		getLogger().info(String.format(message, params));
	}

	public static void warn(String message, Object... params) {
		getLogger().warn(String.format(message, params));
	}

	public static void error(String message, Object... params) {
		getLogger().error(String.format(message, params));
	}

	public static void fatal(String message, Object... params) {
		getLogger().fatal(String.format(message, params));
	}

}
