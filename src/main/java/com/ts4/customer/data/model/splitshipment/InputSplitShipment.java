package com.ts4.customer.data.model.splitshipment;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InputSplitShipment {
    private List<Product> products;
    private String storeId;
    private String postalCode;
    private ArrayList<Object> bonusProducts = new ArrayList<>();
    private boolean deliveryInStore;
    private double subtotalOrder;
    private String action;
}
