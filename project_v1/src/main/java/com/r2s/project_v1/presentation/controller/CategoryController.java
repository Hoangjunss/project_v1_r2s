package com.r2s.project_v1.presentation.controller;

import com.r2s.project_v1.application.dto.product.CategoryCreateDTO;
import com.r2s.project_v1.application.dto.product.CategoryDTO;
import com.r2s.project_v1.application.service.CategoryApplicationService;
import com.r2s.project_v1.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryApplicationService categoryService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> create(
            @RequestBody CategoryCreateDTO createCategoryRequest) {

        return new ResponseEntity<>(categoryService.createCategory(createCategoryRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    @PatchMapping()
    public ResponseEntity<?> update(
            @RequestBody CategoryDTO updateCategoryRequest) {

        CategoryDTO updateCategoryResponse=categoryService.updateCategory(updateCategoryRequest);


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
        Page<CategoryDTO> getCategoryResponseList=categoryService.getList(pageable);

        return ResponseEntity.ok(getCategoryResponseList);
    }

}
