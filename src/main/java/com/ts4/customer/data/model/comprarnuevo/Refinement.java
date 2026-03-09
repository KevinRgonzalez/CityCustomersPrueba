package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Refinement {
    public String _type;
    public String attribute_id;
    public String label;
    public ArrayList<Value> values;
}
