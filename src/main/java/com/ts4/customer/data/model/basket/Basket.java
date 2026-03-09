package com.ts4.customer.data.model.basket;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@lombok.Data
@JsonPropertyOrder({"adjusted_merchandize_total_tax","adjusted_shipping_total_tax","agent_basket","basket_id","billing_address","bonus_discount_line_items","coupon_items","currency","customer_info","gift_certificate_items","grouped_tax_items","inventory_reservation_expiry","merchandize_total_tax","notes","order_price_adjustments","order_total","payment_instruments","product_items","product_sub_total","channel_type","product_total","shipments","shipping_items","creation_date","last_modified","shipping_total","shipping_total_tax","source_code","tax_rounded_at_group","tax_total","taxation"})
public class Basket {

	@JsonProperty("adjusted_merchandize_total_tax")
	private Double adjustedMerchandizeTotalTax;
		
	@JsonProperty("adjusted_shipping_total_tax")
	private Double  adjustedShippingTotalTax;
	
	@JsonProperty("agent_basket")
	private Boolean agentBasket;
		
	@JsonProperty("basket_id")
	private String basketId;
		
	@JsonProperty("billing_address")
	private IngAddress billingAddress;
	
	@JsonProperty("bonus_discount_line_items")
	private List<BonusDiscountLineItem> bonusDiscountLineItems;
	
	@JsonProperty("coupon_items")
	private List<CouponItem> couponItems;
	
	private String currency;
	
	@JsonProperty("customer_info")
	private CustomerInfo customerInfo;
	
	@JsonProperty("gift_certificate_items")
	private List<GiftCertificateItem> giftCertificateItems;
	
	@JsonProperty("grouped_tax_items")
	private List<GroupedTaxItem> groupedTaxItems;
	
	@JsonProperty("inventory_reservation_expiry")
	private String inventoryReservationExpiry;
	
	@JsonProperty("merchandize_total_tax")
	private Double merchandizeTotalTax;
	
	private Notes notes;
	
	@JsonProperty("order_price_adjustments")
	private List<PriceAdjustment> orderPriceAdjustments;
	
	@JsonProperty("order_total")
	private Double orderTotal;
	
	@JsonProperty("payment_instruments")
	private List<PaymentInstrument> paymentInstruments;
	
	@JsonProperty("product_items")
	private List<Item> productItems;
	
	@JsonProperty("product_sub_total")
	private Double productSubTotal;
	
	@JsonProperty("channel_type")
	private String channelType;
	
	@JsonProperty("product_total")
	private Double productTotal;
	
	private List<Shipment> shipments;
	
	@JsonProperty("shipping_items")
	private List<ShippingItem> shippingItems;
	
	@JsonProperty("creation_date")
	private String creationDate;
	
	@JsonProperty("last_modified")
	private String lastModified;
	
	@JsonProperty("shipping_total")
	private Double shippingTotal;
	
	@JsonProperty("shipping_total_tax")
	private Double shippingTotalTax;
	
	@JsonProperty("source_code")
	private String sourceCode;
	
	@JsonProperty("tax_rounded_at_group")
	private boolean taxRoundedAtGroup;
	
	@JsonProperty("tax_total")
	private Double taxTotal;
	
	private String taxation;		
	private String c_customerGroups;
	private String c_storeID;
	
	private String c_cashAmount ;
	private String c_eMoneyAmount ;
	private String c_loyaltyPromotions ;
	
	private String c_nonGroceryInstalmentsOptions;
	private String c_groceryInstalmentsOptions;
	
	private String c_parentProductID;
	
}
