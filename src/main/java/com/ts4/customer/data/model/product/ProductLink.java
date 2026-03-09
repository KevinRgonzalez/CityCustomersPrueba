package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductLink {

    @JsonProperty("source_product_id")
    private String sourceProductId;
    
    @JsonProperty("source_product_link")
    private String sourceProductLink;
    
    @JsonProperty("target_product_id")
    private String targetProductId;
    
    @JsonProperty("target_product_link")
    private String targetProductLink;
    
    private String type;
}
