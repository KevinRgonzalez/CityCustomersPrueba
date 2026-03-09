package com.ts4.customer.data.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import feign.Headers;


@FeignClient(name = "RegisterCustomerClient", url = "${cityclub.url.customer-data}")
public interface CustomerDataClient {

	@PostMapping
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String>  registerCustomer( @RequestBody Object requestBod , 											  
											  @RequestHeader("Authorization") String authorizationHeader,
											  @RequestHeader(value="sfcc-cc-action",required=false)String actionCustomer);	

		
	@GetMapping("/{customer_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String>  getCustomerById(    @RequestHeader("Authorization") String authorizationHeader ,
												@PathVariable("customer_id")    String customer_id ,
												@RequestParam Map<String,Object> queryParams );	
	
	
	@PatchMapping("/{customer_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String>  updateCustomerById( @RequestHeader("Authorization") String authorizationHeader ,
												@RequestHeader(value="sfcc-cc-action",required =false ) String actionCustomer ,
												@PathVariable("customer_id")    String customer_id,
												@RequestBody(required = false) Object customer  );

										
}

