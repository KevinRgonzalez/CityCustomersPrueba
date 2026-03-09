package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class GiftCertificateItemOrder {
	@JsonProperty("amount")
	private Double amount;
	
	@JsonProperty("gift_certificate_item_id")
	private String giftCertificateItemId;
	
	private String message;
	
	@JsonProperty("recipient_email")
	private String recipientEmail;
	
	@JsonProperty("recipient_name")
	private String recipientName;
	
	@JsonProperty("sender_name")
	private String senderName;
	
	@JsonProperty("shipment_id")
	private String shipmentId;
}
