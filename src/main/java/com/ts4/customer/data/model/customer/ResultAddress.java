package com.ts4.customer.data.model.customer;

import java.util.List;

import lombok.Data;

@Data
public class ResultAddress {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<Address>data;
}
