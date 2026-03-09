package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hit{
    private String _type;
    private Data data;
    private double relevance;
}
