package com.ts4.customer.data.model.membership.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class MembershipDetailsResp {
		@JsonProperty("cve_RespCode")
	    private String cveRespCode;
		
		@JsonProperty("desc_MensajeError")
	    private String descMensajeError;
	    private String clientName;
	    private String clientEmail;
	    private String membershipID;
	    
	    private String membershipToken;
	    private String membershipType;
	    private String membershipExpirationDate;
	    private MembershipAddressResp membershipAddress;
	    private List<AdditionalMembershipResp> additionalMemberships;
	    private List<BalanceResp> balances;
}

