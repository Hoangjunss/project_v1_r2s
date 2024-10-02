package com.r2s.project_v1.dto.response.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductResponse {
    private Integer id;
    private String name;
    private Double price;
    private CreateCategoryResponse category;
    private String file;
}
