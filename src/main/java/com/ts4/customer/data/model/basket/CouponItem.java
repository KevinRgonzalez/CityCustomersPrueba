package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CouponItem {
   
	private String code;
    
    @JsonProperty("coupon_item_id")
    private String couponItemId;
    
    @JsonProperty("status_code")
    private String statusCode;
    
    private boolean valid;
}
