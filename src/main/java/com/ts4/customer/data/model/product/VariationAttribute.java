package com.ts4.customer.data.model.product;

import java.util.List;

@lombok.Data
public class VariationAttribute {
    private String id;
    private String name;
    private List<VariationAttributeValue> values;
}
