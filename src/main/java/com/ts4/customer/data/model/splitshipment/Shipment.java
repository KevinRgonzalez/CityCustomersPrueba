package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Shipment{
    private String shipmentId;
    private String type;
    private String displayName;
    private boolean promotion;
    private ApplicableShippingMethods applicableShippingMethods;
    private ArrayList<Product> products;
}
