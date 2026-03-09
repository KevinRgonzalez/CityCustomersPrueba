package com.ts4.customer.data.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;
	private HttpStatus status;

	public DefaultException(String message, HttpStatus status) {
		super(message);
		this.message = message;
		this.status = status;
	}

}
