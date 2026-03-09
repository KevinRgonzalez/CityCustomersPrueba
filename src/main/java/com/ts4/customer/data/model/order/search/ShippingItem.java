package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingItem{
    private String _type;
    private double adjusted_tax;
    private double base_price;
    private String item_id;
    private String item_text;
    private double price;
    private double price_after_item_discount;
    private String shipment_id;
    private double tax;
    private double tax_basis;
    private String tax_class_id;
    private double tax_rate;
}
