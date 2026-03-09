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

@FeignClient(name = "BasketShipmentsCustomerElemClient", url = "${cityclub.url.basket}")
public interface BasketShipmentsClient {
	
	
	@PostMapping("/{basket_id}/shipments")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> addShipmentBasket( @RequestHeader("Authorization") String authorizationHeader ,
											  @PathVariable("basket_id") String basket_id ,
											  @RequestBody Object requestBody  );
	
	@DeleteMapping("/{basket_id}/shipments/{shipment_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> deleteShipmentBasket( @RequestHeader("Authorization") String authorizationHeader , 
												 @PathVariable("basket_id") String basket_id ,
												 @PathVariable("shipment_id") String shipment_id);
	
	@PatchMapping("/{basket_id}/shipments/{shipment_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> updateShipmentBasket( @RequestHeader("Authorization") String authorizationHeader ,
												 @PathVariable("basket_id") String basket_id ,							   		 
												 @PathVariable("shipment_id") String shipment_id ,
										   		 @RequestBody Object requestBody   );

}
