package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ProductItem{
    private String _type;
    private double adjusted_tax;
    private double base_price;
    private boolean bonus_product_line_item;
    private boolean gift;
    private String inventory_id;
    private String item_id;
    private String item_text;
    private double price;
    private double price_after_item_discount;
    private double price_after_order_discount;
    private String product_id;
    private String product_name;
    private int quantity;
    private String shipment_id;
    private double tax;
    private double tax_basis;
    private String tax_class_id;
    private double tax_rate;
    private String c_barcode;
    private double c_conversionFactor;
    private String c_exchangePointsOptions;
    private String c_fixedPrice;
    private String c_fromStoreId;
    private boolean c_isInSorianaStock;
    private String c_salesUnit;
    private String c_selectedUnit;
    private ArrayList<PriceAdjustment> price_adjustments;
    private double c_stepQuantity;
}
