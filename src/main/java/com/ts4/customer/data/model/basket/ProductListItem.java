package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class ProductListItem {
	private String id;
	private long priority;

	@JsonProperty("product_details_link")
	private BonusProduct productDetailsLink;

	@JsonProperty("product_list")
	private ProductList productList;

	@JsonProperty("product_list_item_public")
	private Boolean productListItemPublic;

	@JsonProperty("purchased_quantity")
	private Double purchasedQuantity;
	private Double quantity;
	private String type;
}
