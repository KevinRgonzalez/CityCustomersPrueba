package com.ts4.customer.data.model.membership.request;


@lombok.Data
public class MembershipsReq {	
	private String action;
	private String membershipID;
	private String membershipToken;
	private String clientId;
}
