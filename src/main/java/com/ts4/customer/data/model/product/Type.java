package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Type {
    private boolean bundle;
    private boolean item;
    private boolean master;
    private boolean option;
    private boolean set;
    private boolean variant;
    
    @JsonProperty("variant_group")
    private boolean variationGroup;
}
