package com.r2s.project_v1.presentation.mapper;

import com.r2s.project_v1.application.dto.product.CategoryCreateDTO;
import com.r2s.project_v1.application.dto.product.CategoryDTO;

import com.r2s.project_v1.domain.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Chuyển từ CreateCategoryRequest sang Category entity
    public Category convertCategoryCreateDTOToCategory(CategoryCreateDTO createCategoryRequest) {
        return Category.builder()
                .name(createCategoryRequest.getName())
                .build();
    }

    // Chuyển từ Category entity sang CreateCategoryResponse
    public CategoryDTO convertCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    // Chuyển từ UpdateCategoryRequest sang Category entity
    public Category convertCategoryDTOToCategory(CategoryDTO updateCategoryRequest) {
        return Category.builder()
                .id(updateCategoryRequest.getId())
                .name(updateCategoryRequest.getName())
                .build();
    }

}
