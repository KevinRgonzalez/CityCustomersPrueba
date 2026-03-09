package com.ts4.customer.data.exception;


import java.util.Collection;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponseGeneral extends RuntimeException{
	private static final long serialVersionUID = 1L;

	int status;
	String reason;
	Map<String, Collection<String>> headers;
	String body;

	public ExceptionResponseGeneral(int status, String reason,
									Map<String, Collection<String>> headers,
									String body, String message) {
		super(message);
		this.status = status;
		this.reason = reason;
		this.headers = headers;
		this.body = body;
	}
}
