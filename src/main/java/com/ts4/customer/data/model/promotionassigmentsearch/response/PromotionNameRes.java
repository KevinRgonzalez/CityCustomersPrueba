package com.ts4.customer.data.model.promotionassigmentsearch.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PromotionNameRes {
	@JsonProperty("default")
	private String ddefault;
}
