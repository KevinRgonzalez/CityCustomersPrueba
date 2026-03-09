package com.ts4.customer.data.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DefaultException.class)
	ProblemDetail handleProblemDetailException(DefaultException e) {
		return ProblemDetail.forStatusAndDetail(e.getStatus(), e.getMessage());
	}

}
