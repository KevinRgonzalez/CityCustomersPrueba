package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PriceAdjustmentOrder {
	@JsonProperty("applied_discount")
	private AppliedDiscountOrder appliedDiscount;

	@JsonProperty("coupon_code")
	private String couponCode;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("creation_date")
	private String creationDate;

	private Boolean custom;
	@JsonProperty("item_text")
	private String itemText;

	@JsonProperty("last_modified")
	private String lastModified;

	private Boolean manual;
	private Double price;

	@JsonProperty("price_adjustment_id")
	private String priceAdjustmentId;

	@JsonProperty("promotion_id")
	private String promotionId;

	@JsonProperty("promotion_link")
	private String promotionLink;

	@JsonProperty("reason_code")
	private String reasonCode;

}
