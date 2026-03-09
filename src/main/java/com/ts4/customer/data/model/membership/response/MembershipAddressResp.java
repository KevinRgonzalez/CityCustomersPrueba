package com.ts4.customer.data.model.membership.response;

@lombok.Data
public class MembershipAddressResp {
	private String postalCode;
    private String state;
    private String city;
    private String streetName;
    private String streetNumber;
    private String buildingNumber;
    private String details;
    private String colonia;
}