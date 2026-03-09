package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class HomeDelivery{
    private ArrayList<ShippingType> shippingTypes;
}
