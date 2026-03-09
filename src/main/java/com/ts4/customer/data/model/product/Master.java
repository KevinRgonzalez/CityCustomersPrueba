package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Master {
    private String link;
    @JsonProperty("master_id")
    private String masterId;
    
    private boolean orderable;
    private long price;
    
    @JsonProperty("price_max")
    private long priceMax;
    
    @JsonProperty("price_per_unit")
    private long pricePerUnit;
  
    @JsonProperty("price_per_unit_max")
    private long pricePerUnitMax;
    
    private Prices prices;
}
