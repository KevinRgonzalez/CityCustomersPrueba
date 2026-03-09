package com.ts4.customer.data.model.orderproducts.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductsOrderReq {
	
	@JsonProperty("customer_no")
	private String customerNo;
	private String storeId;
	private String postalCode;
	private Boolean deliveryInStore;
}

