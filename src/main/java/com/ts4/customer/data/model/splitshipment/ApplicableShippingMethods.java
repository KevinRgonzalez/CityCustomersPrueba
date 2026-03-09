package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicableShippingMethods{
    private HomeDelivery homeDelivery;
    private Pickup pickup;
}
