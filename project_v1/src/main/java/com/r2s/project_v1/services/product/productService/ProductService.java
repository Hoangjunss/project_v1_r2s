package com.r2s.project_v1.services.product.productService;

import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.dto.response.product.CreateProductResponse;
import com.r2s.project_v1.dto.response.product.GetProductResponse;
import com.r2s.project_v1.dto.response.product.UpdateProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest createProductRequest);
    UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest);
    void deleteProduct(Integer id);
    Page<GetProductResponse> getList(Pageable pageable);
}
