package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inventory{
    private String _type;
    private int ats;
    private boolean backorderable;
    private String id;
    private boolean orderable;
    private boolean preorderable;
    private int stock_level;
}
