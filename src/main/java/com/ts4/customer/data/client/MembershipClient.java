package com.ts4.customer.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MembershipClient", url = "${cityclub.url.membership}")
public interface MembershipClient {

		@PostMapping
		ResponseEntity<String> getMembershipDetails( @RequestHeader HttpHeaders httpHeaders, @RequestBody Object object);
		
}
