package com.ts4.customer.data.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import feign.Headers;


@FeignClient(name = "CustomerAddressClient", url = "${cityclub.url.customer-address}")
public interface CustomerAddressClient {

	@PostMapping("/{customer_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String>  addAddressCustomer( 											  
											  @RequestHeader("Authorization") String authorizationHeader,
											  @RequestHeader(value="sfcc-cc-action",required=false)String actionCustomer,
											  @RequestBody Object requestBody ,
											  @PathVariable("customer_id")    String customer_id);	

		
	@GetMapping("/{customer_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String>  getAllAddressCustomer(    
												@RequestHeader("Authorization") String authorizationHeader ,
												@RequestHeader(value="sfcc-cc-action",required=false)String actionCustomer,
												@PathVariable("customer_id")    String customer_id );											

	
}

