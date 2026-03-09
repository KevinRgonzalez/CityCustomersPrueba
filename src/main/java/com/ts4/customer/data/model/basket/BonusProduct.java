package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class BonusProduct {
    private String link;
    
    @JsonProperty("product_description")
    private String productDescription;
    
    @JsonProperty("product_id")
    private String productId;
    
    
    @JsonProperty("product_name")
    private String productName;
    
    private String title;
}
