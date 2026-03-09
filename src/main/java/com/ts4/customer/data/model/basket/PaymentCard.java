package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PaymentCard {    
    @JsonProperty("card_type")
    private String cardType;
    
    @JsonProperty("credit_card_expired")
    private Boolean creditCardExpired;
    
    @JsonProperty("credit_card_token")
    private String creditCardToken;
    
    @JsonProperty("expiration_month")
    private Integer expirationMonth;
    
    @JsonProperty("expiration_year")
    private Integer expirationYear;
    
    @JsonProperty("issue_number")
    private String issueNumber;
    
    @JsonProperty("masked_number")
    private String maskedNumber;
    
    @JsonProperty("number_last_digits")
    private String numberLastDigits;
    
    @JsonProperty("valid_from_month")
    private Integer validFromMonth;
    
    @JsonProperty("valid_from_year")
    private Integer validFromYear;

    private String holder;
}
