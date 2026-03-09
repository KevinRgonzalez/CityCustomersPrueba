package com.ts4.customer.data.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Recommendation {
    @JsonProperty("callout_msg")
    private String calloutMsg;

    private Image image;

    @JsonProperty("long_description")
    private String longDescription;

    private String name;

    @JsonProperty("recommendation_type")
    private RecommendationType recommendationType;

    @JsonProperty("recommended_item_id")
    private String recommendedItemId;

    @JsonProperty("recommended_item_link")
    private String recommendedItemLink;

    @JsonProperty("short_description")
    private String shortDescription;
}
