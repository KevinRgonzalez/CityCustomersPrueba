package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputComprarNuevo {
    private String customer_no;
    private Integer storeId;
    private String postalCode;
    private boolean deliveryInStore;
}
