package com.r2s.project_v1.controller;

import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.dto.request.user.AuthenticationRequest;
import com.r2s.project_v1.dto.request.user.CreateUserRequest;
import com.r2s.project_v1.dto.request.user.RefreshToken;
import com.r2s.project_v1.dto.response.product.CreateProductResponse;
import com.r2s.project_v1.dto.response.product.GetProductResponse;
import com.r2s.project_v1.dto.response.product.UpdateProductResponse;
import com.r2s.project_v1.dto.response.user.AuthenticationResponse;
import com.r2s.project_v1.services.product.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @PreAuthorize("hasRole('ADMIN') ")
    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CreateProductRequest createProductRequest) {

        return new ResponseEntity<>(productService.createProduct(createProductRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN') ")
    @PatchMapping()
    public ResponseEntity<?> update(
            @RequestBody UpdateProductRequest updateProductRequest) {

        UpdateProductResponse updateProductResponse=productService.updateProduct(updateProductRequest);


        return ResponseEntity.ok(updateProductResponse);
    }
    @PreAuthorize("hasRole('ADMIN') ")
   @DeleteMapping()
    public ResponseEntity<?> delete(
            @RequestParam Integer id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(true);
    }
    @PreAuthorize("hasRole('USER') ")
    @GetMapping()
    public ResponseEntity<?> getAll() {

        List<GetProductResponse> getProductResponse=productService.getList();

        return ResponseEntity.ok(getProductResponse);
    }

}
