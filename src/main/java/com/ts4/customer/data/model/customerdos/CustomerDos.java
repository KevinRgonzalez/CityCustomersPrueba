package com.ts4.customer.data.model.customerdos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ts4.customer.data.model.customer.Address;
import com.ts4.customer.data.model.customer.PaymentInstrument;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDos {

    @JsonProperty("auth_type")
    private String authType;

    private String birthday;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("customer_no")
    private String customerNo;

    @JsonProperty("email")
    private String email;

    private boolean enabled;
    private String fax;

    @JsonProperty("first_name")
    private String firstName;

    private long gender;

    @JsonProperty("hashed_login")
    private String hashedLogin;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("last_login_time")
    private String lastLoginTime;

    @JsonProperty("last_modified")
    private String lastModified;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("last_visit_time")
    private String lastVisitTime;

    private String login;
    private String note;

    @JsonProperty("payment_instruments")
    private List<PaymentInstrument> paymentInstruments;

    @JsonProperty("phone_business")
    private String phoneBusiness;

    @JsonProperty("phone_home")
    private String phoneHome;

    @JsonProperty("phone_mobile")
    private String phoneMobile;

    @JsonProperty("preferred_locale")
    private String preferredLocale;

    @JsonProperty("previous_login_time")
    private String previousLoginTime;

    @JsonProperty("previous_visit_time")
    private String previousVisitTime;

    private String salutation;

    @JsonProperty("second_name")
    private String secondName;

    private String suffix;
    private String title;

    @JsonProperty("visit_id")
    private String visitId;



    @JsonProperty("c_cartAbandonedDoNotSendRemiderEmail")
    private Boolean cCartAbandonedDoNotSendRemiderEmail;

    @JsonProperty("c_customerRegisteredDays")
    private String cCustomerRegisteredDays;

    @JsonProperty("c_dateForActivation")
    private Boolean cDateForActivation;

    @JsonProperty("c_defaultDeliveryStore")
    private Boolean cDefaultDeliveryStore;

    @JsonProperty("c_doubleOptinLoyaltyResult")
    private String cDoubleOptinLoyaltyResult;

    @JsonProperty("c_doubleOptinToken")
    private String cDoubleOptinToken;

    @JsonProperty("c_emailConfirmed")
    private Boolean cEmailConfirmed;

    @JsonProperty("c_fatherName")
    private String cFatherName;

    @JsonProperty("c_freeShipping")
    private Boolean cFreeShipping;

    @JsonProperty("c_hasFirstAddMembership")
    private Boolean cHasFirstAddMembership;

    @JsonProperty("c_hasSecondAddMembership")
    private Boolean cHasSecondAddMembership;

    @JsonProperty("c_membershipExpirationDate")
    private String cMembershipExpirationDate;

    @JsonProperty("c_membershipLvl")
    private String cMembershipLvl;

    @JsonProperty("c_membershipNumber")
    private String cMembershipNumber;

    @JsonProperty("c_membershipToken")
    private String cMembershipToken;

    @JsonProperty("c_motherName")
    private String cMotherName;

    @JsonProperty("c_newsletterSubscribed")
    private Boolean cNewsletterSubscribed;

    @JsonProperty("c_omonelCardToken")
    private String cOmonelCardToken;

    @JsonProperty("c_omonelMaskedCard")
    private String cOmonelMaskedCard;

    @JsonProperty("c_omonelNumberCard")
    private String cOmonelNumberCard;

    @JsonProperty("c_postalCodeIntoCheckout")
    private String cPostalCodeIntoCheckout;

    @JsonProperty("c_preferredPostalCode")
    private String cPreferredPostalCode;

    @JsonProperty("c_preferredStoreId")
    private String cPreferredStoreId;

    @JsonProperty("c_showBuyMembershipPopup")
    private Boolean cShowBuyMembershipPopup;

    @JsonProperty("c_showErrorMessage")
    private Boolean cShowErrorMessage;

    @JsonProperty("c_showInfographicCheckoutAddCard")
    private Boolean cShowInfographicCheckoutAddCard;

    @JsonProperty("c_showInfographicCheckoutBancomer")
    private Boolean cShowInfographicCheckoutBancomer;

    @JsonProperty("c_showInfographicCheckoutCvv")
    private Boolean cShowInfographicCheckoutCvv;

    @JsonProperty("c_showInfographicCheckoutSantander")
    private Boolean cShowInfographicCheckoutSantander;

    @JsonProperty("c_showInfographicPaymentMethods")
    private Boolean cShowInfographicPaymentMethods;

    @JsonProperty("c_showInfographicPaymentMethodsBancomer")
    private Boolean cShowInfographicPaymentMethodsBancomer;

    @JsonProperty("c_showInfographicPaymentMethodsSantander")
    private Boolean cShowInfographicPaymentMethodsSantander;

    @JsonProperty("c_skipLoginTokenCard")
    private Boolean cSkipLoginTokenCard;

    @JsonProperty("c_smsVerified")
    private Boolean cSmsVerified;

    @JsonProperty("c_tempPostalCode")
    private String cTempPostalCode;

    @JsonProperty("c_tempStoreId")
    private String cTempStoreId;

    @JsonProperty("c_tokenCOOmonel")
    private String cTokenCoOmonel;

    @JsonProperty("detail")
    private String detail;

}
