package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class InputOrderSearch {

    private Query query;
    private String select;
    private ArrayList<Sort> sorts;
    private int count;

    @Getter
    @Setter
    public class BoolQuery{
        private ArrayList<Must> must;
    }

    @Getter
    @Setter
    public class Must{
        private TermQuery term_query;
    }

    @Getter
    @Setter
    public class Query{
        private BoolQuery bool_query;
    }

    @Getter
    @Setter
    public class Sort{
        private String field;
        private String sort_order;
    }

    @Getter
    @Setter
    public class TermQuery{
        private ArrayList<String> fields;
        private String operator;
        private List<String> values;
    }
}
