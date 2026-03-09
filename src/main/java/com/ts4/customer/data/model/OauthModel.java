package com.ts4.customer.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OauthModel(@JsonProperty("access_token") String accessToken,
                         @JsonProperty("token_type") String tokenType, String scope, @JsonProperty("expires_in") long expiresIn) {

}
