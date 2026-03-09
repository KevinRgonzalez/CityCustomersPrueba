package com.ts4.customer.data.model.basket;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ShippingItem {
	@JsonProperty("adjusted_tax")
	private Double adjustedTax;

	@JsonProperty("base_price")
	private Double basePrice;

	@JsonProperty("item_id")
	private String itemId;

	@JsonProperty("item_text")
	private String itemText;

	private Double price;
	
	@JsonProperty("price_adjustments")
	private List<PriceAdjustment> priceAdjustments;

	@JsonProperty("price_after_item_discount")
	private Double priceAfterItemDiscount;

	@JsonProperty("shipment_id")
	private String shipmentId;

	private Double tax;
	@JsonProperty("tax_basis")
	private Double taxBasis;

	@JsonProperty("tax_class_id")
	private String taxClassId;

	@JsonProperty("tax_rate")
	private Double taxRate;
}
