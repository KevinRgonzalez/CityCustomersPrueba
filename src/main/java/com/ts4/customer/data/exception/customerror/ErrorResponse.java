package com.ts4.customer.data.exception.customerror;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	 
	  private int code;
	  private String message;
	  private String stackTrace;
	  private List<ValidationError> errors;
	  
	  
	  @Getter
	  @Setter
	  @RequiredArgsConstructor
	  private static class ValidationError {
		   private final String field;
		   private final String message;
	  }
	  	  
	  public void addValidationError(String field, String message){
	    if(Objects.isNull(errors)) errors = new ArrayList<>();	   
	    errors.add(new ValidationError(field, message));
	  }
}
