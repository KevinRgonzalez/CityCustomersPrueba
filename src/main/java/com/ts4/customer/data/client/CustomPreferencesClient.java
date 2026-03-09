package com.ts4.customer.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import feign.Headers;

@FeignClient(name = "CustomPreferencesClient", url = "${cityclub.url.custompreferences}")
public interface CustomPreferencesClient {	
	
	@GetMapping("/{group_id}/{instance_type}/preferences/{preference_id}")
	@Headers({ "Content-Type: application/json" ,"Accept: application/json"})
	ResponseEntity<String> getCustomPreferences(
			@PathVariable("group_id") String group_id,
			@PathVariable("instance_type") String instance_type,
			@PathVariable("preference_id") String preference_id );
}
