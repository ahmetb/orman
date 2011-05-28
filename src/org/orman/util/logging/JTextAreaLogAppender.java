package org.orman.util.logging;

import javax.swing.JTextArea;

/**
 * Appends logs to a given {@link JTextArea} item on Java Swing GUI. Default
 * level is <code>LoggingLevel.WARN</code>. Print format:
 * <p>
 * <pre>[miliseconds passed] [level] Message</pre>
 * </p>
 * "msecs passed" is counted after initialization of logger.
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 */
public class JTextAreaLogAppender implements ILogger {

	private long start;
	private JTextArea textArea;
	private LoggingLevel level = LoggingLevel.WARN;

	public JTextAreaLogAppender(JTextArea textArea) {
		this.textArea = textArea;
		start = System.currentTimeMillis();
	}

	public void trace(String message, Object... params) {
		if (isLoggable(LoggingLevel.TRACE))
			log("TRACE", String.format(message, params));
	}

	public void debug(String message, Object... params) {
		if (isLoggable(LoggingLevel.DEBUG))
			log("DEBUG", String.format(message, params));
	}

	public void info(String message, Object... params) {
		if (isLoggable(LoggingLevel.INFO))
			log("INFO", String.format(message, params));
	}

	public void warn(String message, Object... params) {
		if (isLoggable(LoggingLevel.WARN))
			log("WARN", String.format(message, params));
	}

	public void error(String message, Object... params) {
		if (isLoggable(LoggingLevel.ERROR))
			log("ERROR", String.format(message, params));
	}

	public void fatal(String message, Object... params) {
		if (isLoggable(LoggingLevel.FATAL))
			log("FATAL", String.format(message, params));
	}

	private boolean isLoggable(LoggingLevel logLevel) {
		return (this.level.getValue() <= logLevel.getValue());
	}

	public void setLevel(LoggingLevel level) {
		this.level = level == null ? LoggingLevel.WARN : level;
	}

	private void log(String level, Object message) {
		textArea.append((System.currentTimeMillis() - start) + " [" + level
				+ "] " + message + '\n');
	}
}
