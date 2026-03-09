package com.ts4.customer.data.model.product;

import java.util.List;

@lombok.Data
public class Option {
    private String description;
    private String id;
    private String image;
    private String name;
    private List<OptionValue> values;
}
