package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductListOrder {
    private String description;
    private String link;
    private String name;
    
    @JsonProperty("product_list_public")
    private Boolean productListPublic;
    
    private String title;
    private String type;
}
