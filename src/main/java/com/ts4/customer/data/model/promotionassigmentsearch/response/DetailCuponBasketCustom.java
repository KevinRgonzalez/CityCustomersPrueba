package com.ts4.customer.data.model.promotionassigmentsearch.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DetailCuponBasketCustom {
	@JsonProperty("callout_msg")
	private CalloutMsgReq calloutMsg;

	@JsonProperty("end_date")
	private String endDate;

	private String details;
	private PromotionNameRes name;
	private String id;

	@JsonProperty("start_date")
	private String startDate;

	@JsonProperty("promotion_class")
	private String promotionClass;
	

}
