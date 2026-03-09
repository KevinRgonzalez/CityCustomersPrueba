package com.ts4.customer.data.model.order.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class OrderResponse {
    @JsonProperty("adjusted_merchandize_total_tax")
    private Double adjustedMerchandizeTotalTax;
    
    @JsonProperty("adjusted_shipping_total_tax")
    private Double adjustedShippingTotalTax;
    
    @JsonProperty("agent_basket")
    private Boolean agentBasket;
    
    @JsonProperty("basket_id")
    private String basketId;
    
    @JsonProperty("billing_address")
    private IngAddressOrder billingAddress;

    @JsonProperty("bonus_discount_line_items")
    private List<BonusDiscountLineItemOrder> bonusDiscountLineItems;
    
    @JsonProperty("coupon_items")
    private List<CouponItemOrder> couponItems;
    
    private String currency;
    @JsonProperty("customer_info")
   
    private CustomerInfoOrder customerInfo;
    
    @JsonProperty("gift_certificate_items")
    private List<GiftCertificateItemOrder> giftCertificateItems;
    
    @JsonProperty("grouped_tax_items")
    private List<GroupedTaxItemOrder> groupedTaxItems;
    
    @JsonProperty("inventory_reservation_expiry")
    private String inventoryReservationExpiry;
    
    @JsonProperty("merchandize_total_tax")
    private Double merchandizeTotalTax;
    
    private NotesOrder notes;
    
    @JsonProperty("order_no")
    private String orderNo;
    
    @JsonProperty("order_price_adjustments")
    private List<PriceAdjustmentOrder> orderPriceAdjustments;
    
    @JsonProperty("order_total")
    private Double orderTotal;
    
    @JsonProperty("payment_instruments")
    private List<PaymentInstrumentOrder> paymentInstruments;
    
    @JsonProperty("product_items")
    private List<ProductItemOrder> productItems;
    
    @JsonProperty("product_sub_total")
    private Double productSubTotal;
    
    @JsonProperty("product_total")
    private Double productTotal;
    
    private List<ShipmentOrder> shipments;
    
    @JsonProperty("shipping_items")
    private List<ShippingItemOrder> shippingItems;
    
    @JsonProperty("shipping_total")
    private Double shippingTotal;
    
    @JsonProperty("shipping_total_tax")
    private Double shippingTotalTax;
    
    @JsonProperty("source_code")
    private String sourceCode;
    
    @JsonProperty("tax_rounded_at_group")
    private Boolean taxRoundedAtGroup;
    
    @JsonProperty("tax_total")
    private Double taxTotal;
    
    private String taxation;
    
    @JsonProperty("creation_date")
    private String creationDate;
    
    
    private String c_storeID;
}
