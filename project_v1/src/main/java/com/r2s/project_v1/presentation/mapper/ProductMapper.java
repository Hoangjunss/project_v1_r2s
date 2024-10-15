package com.r2s.project_v1.presentation.mapper;

import com.r2s.project_v1.application.dto.product.ProductCreateDTO;
import com.r2s.project_v1.application.dto.product.ProductDTO;
import com.r2s.project_v1.application.dto.product.ProductUpdateDTO;

import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.models.Product;
import com.r2s.project_v1.domain.models.ProductImage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Chuyển từ CreateProductRequest sang Product entity
    public Product convertProductCreateDTOToProduct(ProductCreateDTO createProductRequest, Category category, ProductImage productImage) {
        return Product.builder()
                .name(createProductRequest.getName())
                .price(createProductRequest.getPrice())
                .category(category) // Category sẽ được lấy từ database trước
                .productImage(productImage) // ProductImage từ file upload hoặc nơi lưu trữ khác
                .build();
    }

    // Chuyển từ Product entity sang CreateProductResponse
    public ProductDTO convertProductToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory().getName())
                .productImage(product.getProductImage().getUrl())// Giả sử ProductImage có thuộc tính 'url'
                .build();
    }

    // Chuyển từ UpdateProductRequest sang Product entity
    public Product convertProductUpdateDTOToProduct(ProductUpdateDTO updateProductRequest, Category category, ProductImage productImage) {
        return Product.builder()
                .id(updateProductRequest.getId())
                .name(updateProductRequest.getName())
                .price(updateProductRequest.getPrice())
                .category(category) // Lấy từ database
                .productImage(productImage) // Giống như trên
                .build();
    }
}

