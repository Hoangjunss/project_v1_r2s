package com.r2s.project_v1.presentation.controller;

import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.GetProductResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateProductResponse;
import com.r2s.project_v1.application.service.ProductApplicationService;
import com.r2s.project_v1.domain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/product")
@RestController
public class ProductController {
    @Autowired
    private ProductApplicationService productService;
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    @PostMapping()
    public ResponseEntity<?> create(
            @ModelAttribute @Valid CreateProductRequest createProductRequest) {

        return new ResponseEntity<>(productService.createProduct(createProductRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    @PatchMapping()
    public ResponseEntity<?> update(
            @ModelAttribute @Valid UpdateProductRequest updateProductRequest) {

        UpdateProductResponse updateProductResponse=productService.updateProduct(updateProductRequest);


        return ResponseEntity.ok(updateProductResponse);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
   @DeleteMapping()
    public ResponseEntity<?> delete(
            @RequestParam Integer id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(true);
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_ADMIN') ")
    @GetMapping()
    public ResponseEntity<?> getAll( @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetProductResponse> getProductResponse=productService.getList(pageable);

        return ResponseEntity.ok(getProductResponse);
    }

}
