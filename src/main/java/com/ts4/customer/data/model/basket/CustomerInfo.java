package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CustomerInfo {
	
	@JsonProperty("customer_id")
    private String customerId;
	
	@JsonProperty("customer_name")
    private String customerName;
	
	@JsonProperty("customer_no")
    private String customerNo;
    private String email;
}
