package com.r2s.project_v1.domain.service;


import com.r2s.project_v1.domain.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Integer id);
    Page<Product> getList(Pageable pageable);
}
