package com.ts4.customer.data.model.comercialstructure;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Datumcomercial {
    private String _type;
    private String id;
    private String name;
    private String page_description;
    private String page_title;
    private String parent_category_id;
    private ArrayList<ParentCategoryTree> parent_category_tree;
    private boolean c_enableCompare;
    private boolean c_hideInResults;
    private boolean c_isCategoryOffer;
    private boolean c_isDiscountCategory;
    private boolean c_promoEnable;
    private boolean c_promoEnableMenu;
    private boolean c_showInMenu;
    private int c_hitCount;
    private boolean c_visible;
}
