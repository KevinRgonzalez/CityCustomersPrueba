package com.ts4.customer.data.client;

import com.ts4.customer.data.model.splitshipment.OutputSplitShipment;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SplittShipmentClient", url = "${cityclub.url.split-shipment}")
public interface SplittShipmentClient {
    @PostMapping
    @Headers({ "Content-Type: application/json" ,"Accept: */*"})
    ResponseEntity<OutputSplitShipment> splitShipment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Object requestBody);
    }