package com.ts4.customer.data.model.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Product {
	private String brand;

	@JsonProperty("bundled_products")
	@SerializedName("bundled_products")
	private List<BundledProduct> bundledProducts;
	
	private String currency;
	private String ean;

	@JsonProperty("fetch_date")
	@SerializedName("fetch_date")
	private long fetchDate;
	
	private String id;

	@JsonProperty("image_groups")
	@SerializedName("image_groups")
	private List<ImageGroup> imageGroups;

	private String c_nameBySite;

	@JsonProperty("c_listBadges")
	@SerializedName("c_listBadges")
	private List<Badge> c_listBadges;
	
	private List<Inventory> inventories;
	private Inventory inventory;

	@JsonProperty("long_description")
	@SerializedName("long_description")
	private String longDescription;

	@JsonProperty("manufacturer_name")
	@SerializedName("manufacturer_name")
	private String manufacturerName;

	@JsonProperty("manufacturer_sku")
	@SerializedName("manufacturer_sku")
	private String manufacturerSku;

	private Master master;

	@JsonProperty("min_order_quantity")
	@SerializedName("min_order_quantity")
	private Double minOrderQuantity;

	private String name;
	private List<Option> options;

	@JsonProperty("page_description")
	@SerializedName("page_description")
	private String pageDescription;

	@JsonProperty("page_keywords")
	@SerializedName("page_keywords")
	private String pageKeywords;

	@JsonProperty("page_title")
	@SerializedName("page_title")
	private String pageTitle;
	
	private long price;

	@JsonProperty("price_max")
	@SerializedName("price_max")
	private long priceMax;

	@JsonProperty("price_per_unit")
	@SerializedName("price_per_unit")
	private long pricePerUnit;

	@JsonProperty("price_per_unit_max")
	@SerializedName("price_per_unit_max")
	private long pricePerUnitMax;
	
	private Prices prices;

	@JsonProperty("primary_category_id")
	@SerializedName("primary_category_id")
	private String primaryCategoryId;

	@JsonProperty("product_links")
	@SerializedName("product_links")
	private List<ProductLink> productLinks;

	@JsonProperty("product_promotions")
	@SerializedName("product_promotions")
	private List<ProductPromotion> productPromotions;
	
	private List<Recommendation> recommendations;

	@JsonProperty("set_products")
	@SerializedName("set_products")
	private List<Object> setProducts;

	@JsonProperty("short_description")
	@SerializedName("short_description")
	private String shortDescription;

	@JsonProperty("step_quantity")
	@SerializedName("step_quantity")
	private Double stepQuantity;
	
	private Type type;
	private String unit;

	@JsonProperty("unit_measure")
	@SerializedName("unit_measure")
	private String unitMeasure;

	@JsonProperty("unit_quantity")
	@SerializedName("unit_quantity")
	private long unitQuantity;
	
	private String upc;
	
	//@SerializedName("valid_from")
	private Object validFrom;
	
	//@SerializedName("valid_to")
	private String validTo;
	private List<Varia> variants;

	@JsonProperty("variation_attributes")
	@SerializedName("variation_attributes")
	private List<VariationAttribute> variationAttributes;

	@JsonProperty("variation_groups")
	@SerializedName("variation_groups")
	private List<Varia> variationGroups;

	@JsonProperty("variation_values")
	@SerializedName("variation_values")
	private Prices variationValues;

	@JsonProperty("c_saleLimit")
	@SerializedName("c_saleLimit")
	private Double saleLimit=null;

	@JsonProperty("c_productNotesEnabled")
	@SerializedName("c_productNotesEnabled")
	private Boolean c_productNotesEnabled;
}