package com.klalit.utils;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.klalit.beans.ErrorBean;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler(Throwable.class)
	protected ResponseEntity<Object> ExceptionHandling(HttpServletResponse response, Throwable exception) {

		exception.printStackTrace();

		if (exception instanceof ApplicationException) {

			// casting the exception into ApplicationException
			ApplicationException appException = (ApplicationException) exception;
			// getting the error message from the enum
			String errorMessage = appException.getErrorType().getErrorDefinition();
			// getting the detailed error text
			String internalMessage = exception.getMessage();
			// getting the error code
			int errorCode = appException.getErrorType().getErrorCode();
			// print a detailed message of error to user
			System.out.println(errorCode + " " + errorMessage + "\n" + internalMessage);
			// creating a new object to return
			ErrorBean errorBean = new ErrorBean(errorCode, internalMessage, errorMessage);

			return new ResponseEntity<Object>(errorBean, HttpStatus.BAD_REQUEST);

		} else {

			// here we handle an exception that we didn't catch and wrapped
			String internalMessage = exception.getMessage();

			ErrorBean errorBean = new ErrorBean(601, internalMessage, "GENERAL_ERROR");

			return new ResponseEntity<Object>(errorBean, HttpStatus.BAD_REQUEST);
		}
	}
}
