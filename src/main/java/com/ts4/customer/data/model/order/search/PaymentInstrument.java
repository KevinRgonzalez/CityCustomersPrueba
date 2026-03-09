package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInstrument{
    private String _type;
    private double amount;
    private String payment_instrument_id;
    private String payment_method_id;
    private String c_expectedMethod;
    private PaymentCard payment_card;
    private String c_bankIDnumber;
    private String c_cardIcon;
    private String c_storedPaymentUUID;
    private AuthorizationStatus authorization_status;
}
