package com.ts4.customer.data.model.promotionassigmentsearch.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CampaignRes {
	@JsonProperty("campaign_id")
    private String campaignID;
	
    private List<String> coupons;
    
    @JsonProperty("creation_date")
    private String creationDate;
    
    @JsonProperty("customer_groups")
    private List<String> customerGroups;
    
    private String description;
    private Boolean enabled;
    @JsonProperty("last_modified")
    private String lastModified;
    
    private String link;
    
    @JsonProperty("start_date")
    private String startDate;
   
    @JsonProperty("end_date")
    private String endDate;
}
