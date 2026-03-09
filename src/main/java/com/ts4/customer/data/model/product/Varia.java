package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Varia {
    private String link;
    private boolean orderable;
    private long price;
    
    @JsonProperty("price_per_unit")
    private long pricePerUnit;
    
    @JsonProperty("product_id")
    private String productId;
    
    @JsonProperty("variation_values")
    private Prices variationValues;
}
