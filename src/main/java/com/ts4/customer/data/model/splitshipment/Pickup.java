package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pickup{
    private String shippingMethodID;
    private String shippingCost;
    private Object shippingTypes;
}
