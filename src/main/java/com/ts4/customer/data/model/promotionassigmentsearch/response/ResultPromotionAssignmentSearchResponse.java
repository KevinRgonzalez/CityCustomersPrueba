package com.ts4.customer.data.model.promotionassigmentsearch.response;

import java.util.List;

import com.ts4.customer.data.model.promotionassigmentsearch.request.QueryCampaing;

import lombok.Data;

@Data
public class ResultPromotionAssignmentSearchResponse {
	private Integer count;
	private List<String>expand;
	private List<HintPromotionAssignmentSearch> hits;
    private QueryCampaing query;
    private String select;
    private Integer start;
    private Integer total;
}
