package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductListItemParentOrder{
    private String id;
    private Integer priority;
    
    @JsonProperty("product_details_link")
    private BonusProductOrder productDetailsLink;
    
    @JsonProperty("product_list")
    private ProductListOrder productList;

    @JsonProperty("public")
    private Boolean productListItemPublic;

    @JsonProperty("purchased_quantity")
    private Double purchasedQuantity;

    private Double quantity;
    private String type;
}
