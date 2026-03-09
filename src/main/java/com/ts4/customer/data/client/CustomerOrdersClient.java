package com.ts4.customer.data.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import feign.Headers;

@FeignClient(name = "CustomerOrders", url = "${cityclub.url.customer-data}")
public interface CustomerOrdersClient {

		@GetMapping("/{customer_id}/orders")
		@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
		ResponseEntity<String>  getOrdersCustomer( 										  
												  @RequestHeader("Authorization") String authorizationHeader,
												  @PathVariable("customer_id") String customer_id  ,@RequestParam Map<String,Object> queryParams 	
												);	
			
}
