package com.r2s.project_v1.controller;

import com.r2s.project_v1.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.dto.response.product.GetProductResponse;
import com.r2s.project_v1.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.dto.response.product.UpdateProductResponse;
import com.r2s.project_v1.services.product.categoryService.CategoryService;
import com.r2s.project_v1.services.product.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PreAuthorize("hasRole('ADMIN') ")
    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CreateCategoryRequest createCategoryRequest) {

        return new ResponseEntity<>(categoryService.createCategory(createCategoryRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN') ")
    @PatchMapping()
    public ResponseEntity<?> update(
            @RequestBody UpdateCategoryRequest updateCategoryRequest) {

        UpdateCategoryResponse updateCategoryResponse=categoryService.updateCategory(updateCategoryRequest);


        return ResponseEntity.ok(updateCategoryResponse);
    }
    @PreAuthorize("hasRole('ADMIN') ")
    @DeleteMapping()
    public ResponseEntity<?> delete(
            @RequestParam Integer id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(true);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<?> getAll() {

        List<GetCategoryResponse> getCategoryResponseList=categoryService.getList();

        return ResponseEntity.ok(getCategoryResponseList);
    }

}
