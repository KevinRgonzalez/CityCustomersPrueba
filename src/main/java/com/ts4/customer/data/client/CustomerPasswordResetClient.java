package com.ts4.customer.data.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;

@FeignClient(name = "CustomerPasswordResetClient", url = "${cityclub.url.customer-reset-password}")
public interface CustomerPasswordResetClient {

	@PostMapping
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> setPasswordReset( @RequestBody Object requestBody , @RequestParam Map<String,Object> queryParams );
	
	
	
}

