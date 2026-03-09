package com.ts4.customer.data.model.basket;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class BonusDiscountLineItem {
	
	@JsonProperty("bonus_products")
    private List<BonusProduct> bonusProducts;

	@JsonProperty("coupon_code")
    private String couponCode;
    private String id;
    
	@JsonProperty("max_bonus_items")
    private Integer maxBonusItems;
	
	@JsonProperty("promotion_id")
    private String promotionId;
}
