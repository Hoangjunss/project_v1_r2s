package com.r2s.project_v1.application.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {

    private String name;
    private Double price;
    private Integer idCategory;
    private MultipartFile file;
}
