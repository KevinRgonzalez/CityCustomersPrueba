package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class RecommendationType {
	@JsonProperty("display_value")
    private String displayValue;
    private long value;
}
