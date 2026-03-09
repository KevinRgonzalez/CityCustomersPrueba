package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class CouponItemOrder {
	private String code;
	
	@JsonProperty("coupon_item_id")
	private String couponItemId;
	
	@JsonProperty("status_code")
	private String statusCode;
	
	private Boolean valid;
}
