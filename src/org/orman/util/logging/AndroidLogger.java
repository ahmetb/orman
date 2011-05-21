package org.orman.util.logging;

import android.util.Log;

/**
 * Uses Android's Log utility to pass logs to the logging stream.
 * Will not log values under given threshold level (default: WARN).
 * 
 * @author ahmet alp balkan <ahmetalpbalkan at gmail.com>
 */
public class AndroidLogger implements ILogger {
	LoggingLevel level = LoggingLevel.WARN;

	private String appTag;

	public AndroidLogger(String applicationTag) {
		this.appTag = applicationTag;
	}

	public void trace(String message, Object... params) {
		if (isLoggable(LoggingLevel.TRACE))
			Log.v(appTag, String.format(message, params));
	}

	public void debug(String message, Object... params) {
		if (isLoggable(LoggingLevel.DEBUG))
			Log.d(appTag, String.format(message, params));
	}

	public void info(String message, Object... params) {
		if (isLoggable(LoggingLevel.INFO))
			Log.i(appTag, String.format(message, params));
	}

	public void warn(String message, Object... params) {
		if (isLoggable(LoggingLevel.WARN))
			Log.w(appTag, String.format(message, params));
	}

	public void error(String message, Object... params) {
		if (isLoggable(LoggingLevel.ERROR))
			Log.e(appTag, String.format(message, params));
	}

	public void fatal(String message, Object... params) {
		if (isLoggable(LoggingLevel.FATAL))
			Log.e(appTag, String.format(message, params));
	}
	
	private boolean isLoggable(LoggingLevel logLevel){
		return (this.level.getValue() <= logLevel.getValue());
	}

	@Override
	public void setLevel(LoggingLevel level) {
		this.level = level == null ? LoggingLevel.WARN : level;
	}

}
