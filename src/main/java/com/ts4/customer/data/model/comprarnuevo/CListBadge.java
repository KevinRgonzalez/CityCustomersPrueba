package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CListBadge{
    private String url;
    private String displayName;
    private String displayTitle;
    private String displayColor;
    private String promotionId;
    private Integer groupPosition = 1;
    private boolean success;
    private ArrayList<Object> parametersError;
    private ArrayList<String> promotionParameters;
    private String code;
}
