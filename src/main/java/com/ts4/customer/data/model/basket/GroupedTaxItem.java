package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class GroupedTaxItem {
	private String _type;
	@JsonProperty("tax_rate")
    private Double taxRate;
	
	@JsonProperty("tax_value")
    private Double taxValue;
}
