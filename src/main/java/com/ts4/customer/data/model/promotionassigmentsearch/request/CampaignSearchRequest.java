package com.ts4.customer.data.model.promotionassigmentsearch.request;

import java.util.List;

@lombok.Data
public class CampaignSearchRequest {
    private Integer count;
    private QueryCampaing query;
    private List<String> expand;
    private String select;
    private Integer start;
}
