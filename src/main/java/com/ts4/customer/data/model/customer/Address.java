package com.ts4.customer.data.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
	private String address1;
	private String address2;

	@JsonProperty("address_id")
	private String addressId;

	private String city;

	@JsonProperty("company_name")
	private String companyName;

	@JsonProperty("country_code")
	private String countryCode;

	@JsonProperty("creation_date")
	private String creationDate;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("job_title")
	private String jobTitle;

	@JsonProperty("last_modified")
	private String lastModified;

	@JsonProperty("last_name")
	private String lastName;

	private String phone;

	@JsonProperty("post_box")
	private String postBox;

	@JsonProperty("postal_code")
	private String postalCode;

	private Boolean preferred;
	private String salutation;

	@JsonProperty("second_name")
	private String secondName;

	@JsonProperty("state_code")
	private String stateCode;

	private Boolean c_isMembershipAddress;
	@JsonProperty("c_streetNumber")
	private String streetNumber;

	private String suffix;
	private String suite;
	private String title;
	
	@JsonProperty("c_reference")
	private String cReference;
	
	@JsonProperty("c_colonia")
	private String cColonia;
	
	@JsonProperty("c_buildingNumber")
	private String cBuildingNumber;
	
	
	
}
