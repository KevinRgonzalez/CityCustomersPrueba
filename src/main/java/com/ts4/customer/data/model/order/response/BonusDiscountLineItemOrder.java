package com.ts4.customer.data.model.order.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class BonusDiscountLineItemOrder {
    @JsonProperty("bonus_products")
    private List<BonusProductOrder> bonusProducts;
   
    @JsonProperty("coupon_code")
    private String couponCode;
    
    private String id;
    
    @JsonProperty("max_bonus_items")
    private Integer maxBonusItems;
    
    @JsonProperty("promotion_id")
    private String promotionId;
}
