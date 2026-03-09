package com.ts4.customer.data.model.badge;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BadgeConfig {
    private String displayName;
    private String displayTitle;
    private String displayColor;
    private String promotionId;
    private Integer groupPosition;
    private String code;
}