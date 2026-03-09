package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GeneralBasket {
    @JsonProperty("_v")
    private String v;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("baskets")
    private ArrayList<Basket> baskets;
    @JsonProperty("total")
    private int total;
}
