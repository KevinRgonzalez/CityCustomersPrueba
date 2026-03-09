package com.ts4.customer.data.model.order.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ShippingMethodOrder {
	private String description;
	
	@JsonProperty("external_shipping_method")
	private String externalShippingMethod;
	
	private String id;
	private String name;
	private Double price;
	
	@JsonProperty("shipping_promotions")
	private List<ShippingPromotionOrder> shippingPromotions;
}
