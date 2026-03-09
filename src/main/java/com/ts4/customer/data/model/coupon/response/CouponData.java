package com.ts4.customer.data.model.coupon.response;

import lombok.Data;

@Data
public class CouponData {
	private String id;
	private String start_date;
	private String callout_msg;
	private String end_date;
	private String details;
	private String name;
}
