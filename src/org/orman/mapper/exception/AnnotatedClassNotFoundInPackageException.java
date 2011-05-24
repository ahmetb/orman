package org.orman.mapper.exception;

public class AnnotatedClassNotFoundInPackageException extends RuntimeException {
	private String packageName;
	
	public AnnotatedClassNotFoundInPackageException(String packageName) {
		this.packageName = packageName;
	}
	
	public String getMessage() {
		return String.format("Could not find any entity marked class in \'%s\'",packageName);
	}
}
