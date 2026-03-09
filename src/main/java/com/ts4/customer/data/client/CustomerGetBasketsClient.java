package com.ts4.customer.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import feign.Headers;

@FeignClient(name = "CustomerGetBaskets", url = "${cityclub.url.customer-baskets}")
public interface CustomerGetBasketsClient {
	
	@GetMapping("/{customer_id}/baskets")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> getBasketsCustomer( @RequestHeader("Authorization") String authorizationHeader ,
											   @RequestHeader(value="sfcc-cc-action",required =false ) String actionCustomer ,												
											   @PathVariable("customer_id")String customer_id);


	@DeleteMapping("/baskets/{basket_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> deleteBasketsCustomer( @RequestHeader("Authorization") String authorizationHeader ,
											   @PathVariable("basket_id")String basket_id);

}
