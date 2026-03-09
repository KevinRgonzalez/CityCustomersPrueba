package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class AppliedDiscountOrder {
    private Double amount;
    private Double percentage;
    
    @JsonProperty("price_book_id")
    private String priceBookId;
    private String type;
}
