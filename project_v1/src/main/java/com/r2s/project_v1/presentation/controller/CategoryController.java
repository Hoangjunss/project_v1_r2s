package com.r2s.project_v1.presentation.controller;

import com.r2s.project_v1.application.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.application.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.application.service.CategoryApplicationServiceImpl;
import com.r2s.project_v1.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryApplicationServiceImpl categoryService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CreateCategoryRequest createCategoryRequest) {

        return new ResponseEntity<>(categoryService.createCategory(createCategoryRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    @PatchMapping()
    public ResponseEntity<?> update(
            @RequestBody UpdateCategoryRequest updateCategoryRequest) {

        UpdateCategoryResponse updateCategoryResponse=categoryService.updateCategory(updateCategoryRequest);


        return ResponseEntity.ok(updateCategoryResponse);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    @DeleteMapping()
    public ResponseEntity<?> delete(
            @RequestParam Integer id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(true);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping()
    public ResponseEntity<?> getAll(  @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetCategoryResponse> getCategoryResponseList=categoryService.getList(pageable);

        return ResponseEntity.ok(getCategoryResponseList);
    }

}
