package com.r2s.project_v1.services.product.categoryService;

import com.r2s.project_v1.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.dto.response.product.UpdateCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);
    UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);
    void deleteCategory(Integer id);
    Page<GetCategoryResponse> getList(Pageable pageable);
    GetCategoryResponse findById(Integer id);
}
