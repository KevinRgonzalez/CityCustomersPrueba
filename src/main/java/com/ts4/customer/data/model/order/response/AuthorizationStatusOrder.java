package com.ts4.customer.data.model.order.response;

@lombok.Data
public class AuthorizationStatusOrder {
    private String code;
    private String message;
    private Integer status;
}
