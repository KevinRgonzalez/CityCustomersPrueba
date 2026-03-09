package com.ts4.customer.data.model.order.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCard{
    private String _type;
    private String card_type;
    private boolean credit_card_expired;
    private String credit_card_token;
    private String masked_number;
    private String number_last_digits;
    private int expiration_month;
    private int expiration_year;
    private String holder;
}
