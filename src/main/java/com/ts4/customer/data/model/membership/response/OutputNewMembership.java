package com.ts4.customer.data.model.membership.response;

import lombok.Data;

@Data
public class OutputNewMembership {
     private String cve_RespCode;
     private String desc_MensajeError;
     private String membershipID;
     private String membershipToken;
     private String membershipType;
     private String membershipText;
}
