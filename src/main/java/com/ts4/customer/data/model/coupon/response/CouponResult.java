package com.ts4.customer.data.model.coupon.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CouponResult {
	private Integer count;
	private List<CouponData>data;
	private Integer total;
}
