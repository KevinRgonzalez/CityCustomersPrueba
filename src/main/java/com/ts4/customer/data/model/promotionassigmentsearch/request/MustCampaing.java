package com.ts4.customer.data.model.promotionassigmentsearch.request;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MustCampaing {
	@JsonProperty("term_query")
    private TermQueryCampaing termQuery;
}
