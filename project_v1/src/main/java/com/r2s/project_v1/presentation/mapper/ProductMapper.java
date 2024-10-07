package com.r2s.project_v1.presentation.mapper;

import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.CreateProductResponse;
import com.r2s.project_v1.application.dto.response.product.GetProductResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateProductResponse;
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
    public Product convertCreateProductRequestToProduct(CreateProductRequest createProductRequest, Category category, ProductImage productImage) {
        return Product.builder()
                .name(createProductRequest.getName())
                .price(createProductRequest.getPrice())
                .category(category) // Category sẽ được lấy từ database trước
                .productImage(productImage) // ProductImage từ file upload hoặc nơi lưu trữ khác
                .build();
    }

    // Chuyển từ Product entity sang CreateProductResponse
    public CreateProductResponse convertProductToCreateProductResponse(Product product) {
        return CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory().getName())
                .productImage(product.getProductImage().getUrl())// Giả sử ProductImage có thuộc tính 'url'
                .build();
    }

    // Chuyển từ UpdateProductRequest sang Product entity
    public Product convertUpdateProductRequestToProduct(UpdateProductRequest updateProductRequest, Category category, ProductImage productImage) {
        return Product.builder()
                .id(updateProductRequest.getId())
                .name(updateProductRequest.getName())
                .price(updateProductRequest.getPrice())
                .category(category) // Lấy từ database
                .productImage(productImage) // Giống như trên
                .build();
    }

    // Chuyển từ Product entity sang GetProductResponse
    public GetProductResponse convertProductToGetProductResponse(Product product) {
        return GetProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory().getName())
                .productImage(product.getProductImage().getUrl())
                .build();
    }

    // Chuyển từ Product entity sang UpdateProductResponse
    public UpdateProductResponse convertProductToUpdateProductResponse(Product product) {
        return UpdateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory().getName())
                .productImage(product.getProductImage().getUrl())
                .build();
    }
}

