package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceAdjustment{
    private String _type;
    private AppliedDiscount applied_discount;
    private String creation_date;
    private boolean custom;
    private String item_text;
    private String last_modified;
    private boolean manual;
    private double price;
    private String price_adjustment_id;
    private String promotion_id;
    private String promotion_link;
}
