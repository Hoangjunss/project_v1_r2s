package com.r2s.project_v1.dto.request.product;

import com.r2s.project_v1.dto.response.product.CreateCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    private String name;
    private String price;
    private Integer idCategory;
}
