package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationStatus{
    private String _type;
    private String code;
    private String message;
    private int status;
}
