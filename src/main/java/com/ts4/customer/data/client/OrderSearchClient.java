package com.ts4.customer.data.client;

import com.ts4.customer.data.model.order.search.OutputOrderSearch;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import feign.Headers;

@FeignClient(name = "OrderSearchClient", url = "${cityclub.url.order-search}")
public interface OrderSearchClient {
	
	/*@PostMapping
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> getOrderSeachsCustomer( @RequestHeader("Authorization") String authorizationHeader ,
										   @PathVariable("basket_id") String basket_id,
										   @RequestBody Object requestBody  );*/

	@PostMapping
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<OutputOrderSearch> getOrderSeachs(@RequestHeader("Authorization") String authorizationHeader ,
													 @RequestBody Object requestBody  );
}
