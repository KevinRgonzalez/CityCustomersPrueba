package com.ts4.customer.data.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "customer-termination", url = "${cityclub.url.customer-termination}")
public interface MembershipTerminationClient {

    @PostMapping
    ResponseEntity<String> terminationMembership(@RequestHeader HttpHeaders httpHeaders, @RequestHeader("Authorization") String authorization, @RequestBody Map<String, String> body);

}
