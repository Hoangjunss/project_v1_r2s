package com.r2s.project_v1.application.service;

import com.r2s.project_v1.application.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.application.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.domain.service.CategoryService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.repository.CategoryRepository;
import com.r2s.project_v1.presentation.mapper.CategoryMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryApplicationServiceImpl  {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;


    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category=categoryMapper.convertCreateCategoryRequestToCategory(createCategoryRequest);
        Category categorySave=categoryService.createCategory(category);
        return categoryMapper.convertCategoryToCreateCategoryResponse(categorySave);
    }


    public UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        Category category=categoryMapper.convertUpdateCategoryRequestToCategory(updateCategoryRequest);
        Category categoryUpdate=categoryService.updateCategory(category);
        return categoryMapper.convertCategoryToUpdateCategoryResponse(categoryUpdate);

    }


    public void deleteCategory(Integer id) {

       categoryService.deleteCategory(id);

    }



    public GetCategoryResponse findById(Integer id) {
        return categoryMapper.convertCategoryToGetCategoryResponse(categoryService.findById(id));
    }




    public Page<GetCategoryResponse> getList(Pageable pageable) {
        try {
            return categoryService.getList(pageable)
                    .map(category -> categoryMapper.convertCategoryToGetCategoryResponse(category));
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }
}



