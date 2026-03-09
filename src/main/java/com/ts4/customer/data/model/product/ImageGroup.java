package com.ts4.customer.data.model.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ImageGroup {
    private List<Image> images;
    
    
    @JsonProperty("variation_attributes")
    private List<VariationAttribute> variationAttributes;
    
    @JsonProperty("view_type")
    private String viewType;
}
