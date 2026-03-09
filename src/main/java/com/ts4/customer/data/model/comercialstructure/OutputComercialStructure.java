package com.ts4.customer.data.model.comercialstructure;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class OutputComercialStructure {
    private String _v;
    private String _type;
    private int count;
    private ArrayList<Datumcomercial> data;
    private int total;
}
