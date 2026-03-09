package com.ts4.customer.data.model.promotionassigmentsearch.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PromotionRes {

	@JsonProperty("creation_date")
	private String creationDate;
	
	@JsonProperty("disable_globally_excluded")
	private String disableGloballyExcluded;
	
	private String enabled;
	private String exclusivity;
	private String id;
	
	@JsonProperty("callout_msg")
	private CalloutMsgReq calloutMsg;
	
	@JsonProperty("last_modified")
	private String lastModified;
	
	private String link;
	
	@JsonProperty("promotion_class")
	private String promotionClass;
	
	@JsonProperty("c_idPromoDescription")
	private String c_idPromoDescription;
	
	private PromotionNameRes name;
}
