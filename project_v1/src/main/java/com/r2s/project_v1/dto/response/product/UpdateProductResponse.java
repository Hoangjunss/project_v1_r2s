package com.r2s.project_v1.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductResponse {

    private Integer id;
    private String name;
    private String price;
    private UpdateCategoryResponse category;
}
