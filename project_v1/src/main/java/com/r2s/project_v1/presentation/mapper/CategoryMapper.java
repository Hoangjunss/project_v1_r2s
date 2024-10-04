package com.r2s.project_v1.presentation.mapper;

import com.r2s.project_v1.application.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.application.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.domain.models.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Chuyển từ CreateCategoryRequest sang Category entity
    public Category convertCreateCategoryRequestToCategory(CreateCategoryRequest createCategoryRequest) {
        return Category.builder()
                .name(createCategoryRequest.getName())
                .build();
    }

    // Chuyển từ Category entity sang CreateCategoryResponse
    public CreateCategoryResponse convertCategoryToCreateCategoryResponse(Category category) {
        return CreateCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    // Chuyển từ UpdateCategoryRequest sang Category entity
    public Category convertUpdateCategoryRequestToCategory(UpdateCategoryRequest updateCategoryRequest) {
        return Category.builder()
                .id(updateCategoryRequest.getId())
                .name(updateCategoryRequest.getName())
                .build();
    }

    // Chuyển từ Category entity sang GetCategoryResponse
    public GetCategoryResponse convertCategoryToGetCategoryResponse(Category category) {
        return GetCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    // Chuyển từ Category entity sang UpdateCategoryResponse
    public UpdateCategoryResponse convertCategoryToUpdateCategoryResponse(Category category) {
        return UpdateCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
