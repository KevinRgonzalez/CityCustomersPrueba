package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductPromotion {
	
	@JsonProperty("callout_msg")
    private String calloutMsg;
	
    private String link;
    
    @JsonProperty("promotion_id")
    private String promotionId;
    
    @JsonProperty("promotional_price")
    private long promotionalPrice;
}
