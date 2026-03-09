package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppliedDiscount{
    private String _type;
    private double amount;
    private int percentage;
    private String type;
}
