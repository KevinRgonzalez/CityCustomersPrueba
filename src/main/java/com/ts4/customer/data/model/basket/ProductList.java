package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductList {
    private String description;
    private String link;
    private String name;
    
    @JsonProperty("product_list_public")
    private boolean productListPublic;
    
    private String title;
    private String type;
}
