package com.ts4.customer.data.model.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class PaymentBankAccountOrder {
	@JsonProperty("drivers_license_last_digits")
	private String driversLicenseLastDigits;

	@JsonProperty("drivers_license_state_code")
	private String driversLicenseStateCode;

	private String holder;

	@JsonProperty("masked_drivers_license")
	private String maskedDriversLicense;

	@JsonProperty("masked_number")
	private String maskedNumber;

	@JsonProperty("number_last_digits")
	private String numberLastDigits;

}
