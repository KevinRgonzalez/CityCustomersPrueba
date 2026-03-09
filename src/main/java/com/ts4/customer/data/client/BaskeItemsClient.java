package com.ts4.customer.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import feign.Headers;

@FeignClient(name = "BasketItemsDataClient", url = "${cityclub.url.basket}")
public interface BaskeItemsClient {
	
		
	@PostMapping("/{basket_id}/items")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> addItemBasket( @RequestHeader("Authorization") String authorizationHeader ,
										   @PathVariable("basket_id") String basket_id,
										   @RequestBody Object requestBody  );
	
		
	@PatchMapping("/{basket_id}/items/{item_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> updateItemBasket( @RequestHeader("Authorization") String authorizationHeader ,
										   @PathVariable("basket_id") String basket_id,
										   @PathVariable("item_id") String item_id,
										   @RequestBody Object requestBody  );
	
	
	@DeleteMapping("/{basket_id}/items/{item_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> deleteItemBasket( @RequestHeader("Authorization") String authorizationHeader ,
										   @PathVariable("basket_id") String basket_id ,
										   @PathVariable("item_id") String item_id
										 );


	@PatchMapping("/{basket_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> updateBasket( @RequestHeader("Authorization") String authorizationHeader ,@PathVariable("basket_id") String basket_id ,
										 @RequestBody Object requestBody );

}
