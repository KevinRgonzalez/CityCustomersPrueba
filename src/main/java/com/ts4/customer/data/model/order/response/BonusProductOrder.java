package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class BonusProductOrder {
	private String link;
	@JsonProperty("product_description")
	private String productDescription;

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("product_name")
	private String productName;

	private String title;
}
