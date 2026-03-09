package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingMethod{
    private String _type;
    private String id;
    private String name;
    private boolean c_storePickupEnabled;
}
