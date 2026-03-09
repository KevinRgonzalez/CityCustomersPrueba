package com.ts4.customer.data.model.membership.response;

@lombok.Data
public class AdditionalMembershipResp {
    private String clientName;
    private String clientEmail;
    private String membershipID;
    private String membershipType;
    private String membershipToken;
    private String membershipExpirationDate;
}

