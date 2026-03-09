package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Shipment {
	
	
	@JsonProperty("adjusted_merchandize_total_tax")
	private Double adjustedMerchandizeTotalTax;

	@JsonProperty("adjusted_shipping_total_tax")
	private Double adjustedShippingTotalTax;

	private Boolean gift;
	
	@JsonProperty("gift_message")
	private String giftMessage;

	@JsonProperty("merchandize_total_tax")
	private Double merchandizeTotalTax;

	@JsonProperty("product_sub_total")
	private Double productSubTotal;

	@JsonProperty("product_total")
	private Double productTotal;

	@JsonProperty("shipment_id")
	private String shipmentId;

	@JsonProperty("shipment_no")
	private String shipmentNo;

	@JsonProperty("shipment_total")
	private Double shipmentTotal;

	@JsonProperty("shipping_address")
	private IngAddress shippingAddress;

	@JsonProperty("shipping_method")
	private ShippingMethod shippingMethod;

	@JsonProperty("shipping_status")
	private String shippingStatus;

	@JsonProperty("shipping_total")
	private Double shippingTotal;

	@JsonProperty("shipping_total_tax")
	private Double shippingTotalTax;

	@JsonProperty("tax_total")
	private Double taxTotal;

	@JsonProperty("tracking_number")
	private String trackingNumber;

	
	private String c_shipmentId;
	private String c_displayName;
	private Double c_redeemedCash;
	private Double c_redeemedElectronicMoney;
	private Double c_shippingErnedPoints;
	private Double c_shippingErnedElectronicMoney;
	
	private String c_applicableShippingMethods;
	private Integer c_displayIndex;
	private String c_fromStoreId;
	private Boolean c_isPromotion;
	private Boolean c_isSelectedByUser;
	private String c_shipmentType;
	private String c_shippingPrice;
	
}
