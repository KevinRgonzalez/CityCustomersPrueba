package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shipment{
    private String _type;
    private double adjusted_merchandize_total_tax;
    private double adjusted_shipping_total_tax;
    private boolean gift;
    private double merchandize_total_tax;
    private double product_sub_total;
    private double product_total;
    private String shipment_id;
    private String shipment_no;
    private double shipment_total;
    private ShippingAddress shipping_address;
    private ShippingMethod shipping_method;
    private String shipping_status;
    private double shipping_total;
    private double shipping_total_tax;
    private double tax_total;
    private String c_applicableShippingMethods;
    private int c_displayIndex;
    private String c_displayName;
    private String c_fromStoreId;
    private boolean c_isPromotion;
    private boolean c_isSelectedByUser;
    private String c_shipmentId;
    private String c_shipmentType;
    private double c_shippingErnedElectronicMoney;
    private int c_shippingErnedPoints;
    private double c_shippingPrice;
    private String c_slotDateTime;
    private String c_slotDay;
    private String c_slotName;
    private String c_slotSelectionTimestamp;
}
