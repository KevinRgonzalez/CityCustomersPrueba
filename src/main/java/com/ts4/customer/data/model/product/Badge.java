package com.ts4.customer.data.model.product;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Badge {
    private String url;
    private String displayName;
    private String displayTitle;
    private String displayColor;
    private String promotionId;
    private boolean success;
    private ArrayList<Object> parametersError;
    private Object promotionParameters;
    private String code;
}
