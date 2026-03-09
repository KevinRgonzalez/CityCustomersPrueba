package com.ts4.customer.data.model.comprarnuevo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ImageGroup{
    private String _type;
    private ArrayList<Image> images;
    private String view_type;
}
