package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sort{
    private String _type;
    private String field;
    private String sort_order;
}
