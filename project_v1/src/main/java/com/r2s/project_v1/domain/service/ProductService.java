package com.r2s.project_v1.domain.service;

import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.CreateProductResponse;
import com.r2s.project_v1.application.dto.response.product.GetProductResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest createProductRequest);
    UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest);
    void deleteProduct(Integer id);
    Page<GetProductResponse> getList(Pageable pageable);
}
