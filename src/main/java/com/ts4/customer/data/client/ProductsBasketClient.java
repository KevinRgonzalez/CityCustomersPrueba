package com.ts4.customer.data.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.ts4.customer.data.model.product.ProductResult;

import feign.Headers;

@FeignClient(name = "ProductsBasketClient", url = "${cityclub.url.basket-products-info}")
public interface ProductsBasketClient {

	@GetMapping("/({ids})")
	@Headers({ "Content-Type: application/json" ,"Accept: */*"})
	ResponseEntity<Object>  getProductsByIds(
			@RequestHeader("Authorization") String authorizationHeader, 
			@PathVariable("ids") List<String> ids,
			@RequestParam Map<String,Object> queryParams);
}
