package com.ts4.customer.data.model.coupon.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CouponRequest {
	private String code;
	
	@JsonProperty("coupon_item_id")
	private String couponItemId;
	
	@JsonProperty("status_code")
	private String statusCode;
	
}
