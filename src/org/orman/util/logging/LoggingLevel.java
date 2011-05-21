package org.orman.util.logging;

public enum LoggingLevel {
	TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5), QUIET(9);

	private int levelIndicator;

	private LoggingLevel(int level) {
		levelIndicator = level;
	}

	public int getValue() {
		return levelIndicator;
	}
}
