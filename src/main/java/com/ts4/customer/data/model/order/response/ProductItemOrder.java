package com.ts4.customer.data.model.order.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductItemOrder {
	@JsonProperty("adjusted_tax")
	private Double adjustedTax;

	@JsonProperty("base_price")
	private Double basePrice;

	@JsonProperty("bonus_discount_line_item_id")
	private String bonusDiscountLineItemId;

	@JsonProperty("bonus_product_line_item")
	private Boolean bonusProductLineItem;

	@JsonProperty("bundled_product_items")
	private List<ProductItemOrder> bundledProductItems;//Este si no cambiarlo por Object

	private Boolean gift;
	@JsonProperty("gift_message")
	private String giftMessage;

	@JsonProperty("inventory_id")
	private String inventoryId;

	@JsonProperty("item_id")
	private String itemId;

	@JsonProperty("item_text")
	private String itemText;

	@JsonProperty("option_items")
	private List<PItemOrder> optionItems;

	private Double price;
	
	@JsonProperty("price_adjustments")
	private List<PriceAdjustmentOrder> priceAdjustments;
	
	@JsonProperty("price_after_item_discount")
	private Double priceAfterItemDiscount;
	
	@JsonProperty("price_after_order_discount")
	private Double priceAfterOrderDiscount;
	
	@JsonProperty("product_id")
	private String productId;
	
	@JsonProperty("product_list_item")
	private ProductListItemParentOrder productListItem;

	@JsonProperty("product_name")
	private String productName;

	private Double quantity;
	
	@JsonProperty("shipment_id")
	private String shipmentId;

	@JsonProperty("shipping_item_id")
	private String shippingItemId;

	private Double tax;

	@JsonProperty("tax_basis")
	private Double taxBasis;

	@JsonProperty("tax_class_id")
	private String taxClassId;

	@JsonProperty("tax_rate")
	private Double taxRate;

}
