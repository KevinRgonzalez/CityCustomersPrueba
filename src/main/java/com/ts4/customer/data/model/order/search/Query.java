package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query{
    private BoolQuery bool_query;
}
