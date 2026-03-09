package com.ts4.customer.data.model.basket;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ShippingMethod {
    private String description;
    
    @JsonProperty("external_shipping_method")
    private String externalShippingMethod;
    
    private String id;
    private String name;
    private Double price;
    
    @JsonProperty("shipping_promotions")
    private List<ShippingPromotion> shippingPromotions;
    
    @JsonProperty("c_storePickupEnabled")
    private Boolean cStorePickupEnabled;
}
