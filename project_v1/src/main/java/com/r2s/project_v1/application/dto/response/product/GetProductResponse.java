package com.r2s.project_v1.application.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductResponse {

    private Integer id;
    private String name;
    private Double price;
    private String category;
    private String productImage;
}
