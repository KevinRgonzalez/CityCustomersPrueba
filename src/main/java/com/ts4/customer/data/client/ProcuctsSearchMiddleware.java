package com.ts4.customer.data.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ProcuctsSearchMiddleware", url = "${cityclub.url.findproducts}")
public interface ProcuctsSearchMiddleware {
    @GetMapping("({ids})")
    @Headers({ "Content-Type: pplication/json; charset=utf-8" ,"Accept: aplication/json; charset=utf-8"})
    ResponseEntity<Object> getProductsByIds(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("ids") List<String> ids,
            @RequestParam("expand") String expand);
}
