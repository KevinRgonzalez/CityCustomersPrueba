package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;


@Data
public class ProductResult {

	@SerializedName("count")
	private Integer count;
	@SerializedName("total")
	private Integer total;
	@SerializedName("data")
	private List<Product> data;

	@JsonProperty("_v")
	@SerializedName("_v")
	private String v;

	@JsonProperty("_type")
	@SerializedName("_type")
	private String type;
}