package com.ts4.customer.data.model.mockrecomender.response;

import java.util.List;

import lombok.Data;

@Data
public class RecommenderCarrouselGeneralResp {
	private Integer count;
	private List<RecommenderCarrouselResp>carrousels;
}
