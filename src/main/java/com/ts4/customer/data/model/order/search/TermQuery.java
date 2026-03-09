package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TermQuery{
    private String _type;
    private ArrayList<String> fields;
    private String operator;
    private ArrayList<String> values;
}
