package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupedTaxItem{
    private String _type;
    private double tax_rate;
    private double tax_value;
}
