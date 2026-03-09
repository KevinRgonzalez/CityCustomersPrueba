package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Image {
    private String alt;
    
    @JsonProperty("dis_base_link")
    private String disBaseLink;
    private String link;
    private String title;
}
