package com.ts4.customer.data.model.promotionassigmentsearch.request;

import java.util.List;

@lombok.Data
public class TermQueryCampaing {
    private List<String> fields;
    private String operator;
    private List<Object> values;
}
