package com.ts4.customer.data.model.comprarnuevo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ValidFrom{
    @JsonProperty("default")
    private Date mydefault;
}
