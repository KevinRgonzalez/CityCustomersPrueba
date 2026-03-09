package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class OutputComprarNuevo {
    private int count;
    private int total;
    private ArrayList<Datum> data;
    private ArrayList<SortingOption> sorting_options;
    private ArrayList<Refinement> refinements;
}
