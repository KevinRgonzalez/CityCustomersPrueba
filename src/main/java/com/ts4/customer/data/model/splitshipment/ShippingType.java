package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingType{
    private String shippingTypeId;
    private String shippingTypeName;
    private String shippingCost;
    private Object shippingTypeNameCarrier;
    private Object slotsDays;
}
