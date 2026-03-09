package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class GroupedTaxItemOrder {
	
	@JsonProperty("tax_rate")
    private Double taxRate;
	
	@JsonProperty("tax_value")
    private Double taxValue;
}
