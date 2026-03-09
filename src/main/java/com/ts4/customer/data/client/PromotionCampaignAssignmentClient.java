package com.ts4.customer.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import feign.Headers;

@FeignClient(name = "PromotionCampaignAssignmentClient", url = "${cityclub.url.promotion-campaign-assignment-search}")
public interface PromotionCampaignAssignmentClient {
	@PostMapping
	@Headers({ "Content-Type: application/json" ,"Accept: */*"})
	ResponseEntity<String>  getPromotionsCampaignAssignmentSearch(
			@RequestHeader("Authorization") String authorizationHeader, 			
			@RequestBody Object requestBody);
}
