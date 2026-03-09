package com.ts4.customer.data.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "CustomerDataSitesSite", url = "${cityclub.url.customer-data-sites}")
public interface CustomerDataSitesSite {
    @DeleteMapping("/{customer_no}")
    @Headers({ "Content-Type: application/json" ,"Accept: application/json"})
    ResponseEntity<String> deleteCustomerByCustomerNo(@RequestHeader("Authorization") String authorization, @RequestHeader("x-dw-client-id") String client_id,
                                                      @PathVariable("customer_no") String customer_no);
}
