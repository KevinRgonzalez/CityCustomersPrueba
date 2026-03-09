package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class AppliedDiscount {
    private Double amount;
    private Double percentage;
    
    @JsonProperty("price_book_id")
    private String priceBookId;
    
    private String type;
}
