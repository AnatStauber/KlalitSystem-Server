package com.klalit.utils;

import com.klalit.enums.ErrorType;

public class ApplicationException extends Exception {

	private ErrorType errorType;

	// a constructor for a self-made exception (no wrapping needed)
	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	// a constructor for a 3rd-party exception (we wrap it and save the data)
	public ApplicationException(Exception e, ErrorType errorType, String message) {
		super(message, e);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

}
