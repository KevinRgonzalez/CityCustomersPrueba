package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class OutputOrderSearch {
    private String _v;
    private String _type;
    private int count;
    private ArrayList<Hit> hits;
    private Next next;
    private Query query;
    private String select;
    private ArrayList<Sort> sorts;
    private int start;
    private int total;
}
