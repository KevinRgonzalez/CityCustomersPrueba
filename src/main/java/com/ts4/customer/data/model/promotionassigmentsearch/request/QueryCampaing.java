package com.ts4.customer.data.model.promotionassigmentsearch.request;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class QueryCampaing {
	
	
	@JsonProperty("bool_query")
    private BoolQueryCampaing boolQuery;
}
