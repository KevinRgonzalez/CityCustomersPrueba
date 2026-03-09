package com.ts4.customer.data.model.basket;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class IngAddress {
    private String address1;
    private String address2;
    private String city;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("first_name")
    private String firstName;
 
    @JsonProperty("full_name")
    private String fullName;
    
    private String id;
    
    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("last_name")
    private String lastName;
    
    private String phone;

    @JsonProperty("post_box")
    private String postBox;

    @JsonProperty("postal_code")
    private String postalCode;
    
    private String salutation;

    @JsonProperty("second_name")
    private String secondName;

    @JsonProperty("state_code")
    private String stateCode;
    
    private String suffix;
    private String suite;
    private String title;
    
    
    private String c_reference;
    private String c_colonia;
    private String c_buildingNumber;
    private String c_streetNumber;
    private String c_latitude;
    private String c_longitude;
    
}
