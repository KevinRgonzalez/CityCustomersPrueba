package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PaymentInstrumentOrder {
	private Double amount;
	
	@JsonProperty("authorization_status")
	private AuthorizationStatusOrder authorizationStatus;

	@JsonProperty("bank_routing_number")
	private String bankRoutingNumber;

	@JsonProperty("masked_gift_certificate_code")
	private String maskedGiftCertificateCode;

	@JsonProperty("payment_bank_account")
	private PaymentBankAccountOrder paymentBankAccount;

	@JsonProperty("payment_card")
	private PaymentCardOrder paymentCard;

	@JsonProperty("payment_instrument_id")
	private String paymentInstrumentId;

	@JsonProperty("payment_method_id")
	private String paymentMethodId;

}
