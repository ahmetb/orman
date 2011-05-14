package org.orman.util.logging;

import android.util.Log;

public class AndroidLogger implements ILogger {

	private String appTag;
	
	public AndroidLogger(String applicationTag) {
		this.appTag = applicationTag;
	}
	
	public void trace(Object message){
		Log.v(appTag, message.toString());
	}
	
	public void trace(String message, Object... params){
		Log.v(appTag, String.format(message, params));
	}
	
	public void debug(Object message){
		Log.d(appTag, message.toString());
	}
	
	public void debug(String message, Object... params){
		Log.d(appTag, String.format(message, params));
	}
	
	public void info(Object message){
		Log.i(appTag, message.toString());
	}
	
	public void info(String message, Object... params){
		Log.i(appTag, String.format(message, params));
	}
	
	public void warn(Object message){
		Log.w(appTag, message.toString());
	}
	
	public void warn(String message, Object... params){
		Log.w(appTag, String.format(message, params));
	}
	
	public void error(Object message){
		Log.e(appTag, message.toString());
	}
	
	public void error(String message, Object... params){
		Log.e(appTag, String.format(message, params));
	}
	
	public void fatal(Object message){
		Log.e(appTag, message.toString());
	}
	
	public void fatal(String message, Object... params){
		Log.e(appTag, String.format(message, params));
	}

	@Override
	public void setLevel(LoggingLevel level) {
		// TODO implement logging level filtering here if needed. but not necessary in Android.
		// TODO they can be set to FATAL on production to hide tracing details.
	}	
	
}
