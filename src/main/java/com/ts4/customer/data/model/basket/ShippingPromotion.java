package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ShippingPromotion {
	@JsonProperty("callout_msg")
    private String calloutMsg;
   
	private String link;
    
	@JsonProperty("promotion_id")
    private String promotionId;
	
	@JsonProperty("promotion_name")
    private String promotionName;
}
