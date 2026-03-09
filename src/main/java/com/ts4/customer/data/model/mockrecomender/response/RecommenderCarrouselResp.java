package com.ts4.customer.data.model.mockrecomender.response;
import java.util.List;
import lombok.Data;

@Data
public class RecommenderCarrouselResp {
    private String  slot;
    private String  recommender;
    private String  title;
    private String  description;
    private Integer count;
    private List<Object>data;
}
