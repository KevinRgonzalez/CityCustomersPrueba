package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class VariationAttributeValue {
    private String description;
    private Image image;
    
    @JsonProperty("image_swatch")
    private Image imageSwatch;
    
    private String name;
    private boolean orderable;
    private String value;
}
