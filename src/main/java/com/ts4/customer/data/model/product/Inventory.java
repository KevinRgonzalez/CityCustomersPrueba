package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Inventory {
    private long ats;
    
    private boolean backorderable;
    private String id;
   
    @JsonProperty("in_stock_date")
    private String inStockDate;
    
    private boolean orderable;
    private boolean preorderable;
   
    @JsonProperty("stock_level")
    private long stockLevel;
}
