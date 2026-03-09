package com.ts4.customer.data.model.promotionassigmentsearch.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HintPromotionAssignmentSearch {
	private String customIdPromotionCampaign;
	
	@JsonProperty("campaign_id")
	private String campaignID;

	@JsonProperty("coupons_based")
	private Boolean couponsBased;

	@JsonProperty("creation_date")
	private String creationDate;

	@JsonProperty("customer_groups_based")
	private Boolean customerGroupsBased;

	private String description;
	private Boolean enabled;

	@JsonProperty("last_modified")
	private String lastModified;

	private String link;
	
	private PromotionRes promotion;
	private CampaignRes campaign;
	
	
	@JsonProperty("promotion_id")
	private String promotionID;

	@JsonProperty("required_qualifier")
	private String requiredQualifier;

	private Map schedule;

	@JsonProperty("source_code_based")
	private Boolean sourceCodeBased;
}
