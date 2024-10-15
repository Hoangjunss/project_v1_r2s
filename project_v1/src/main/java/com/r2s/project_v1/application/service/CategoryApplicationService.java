package com.r2s.project_v1.application.service;

import com.r2s.project_v1.application.dto.product.CategoryCreateDTO;
import com.r2s.project_v1.application.dto.product.CategoryDTO;

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
public class CategoryApplicationService  {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;


    public CategoryDTO createCategory(CategoryCreateDTO createCategoryRequest) {
        Category category=categoryMapper.convertCategoryCreateDTOToCategory(createCategoryRequest);
        Category categorySave=categoryService.createCategory(category);
        return categoryMapper.convertCategoryToCategoryDTO(categorySave);
    }


    public CategoryDTO updateCategory(CategoryDTO updateCategoryRequest) {
        Category category=categoryMapper.convertCategoryDTOToCategory(updateCategoryRequest);
        Category categoryUpdate=categoryService.updateCategory(category);
        return categoryMapper.convertCategoryToCategoryDTO(categoryUpdate);

    }


    public void deleteCategory(Integer id) {

       categoryService.deleteCategory(id);

    }



    public CategoryDTO findById(Integer id) {
        return categoryMapper.convertCategoryToCategoryDTO(categoryService.findById(id));
    }




    public Page<CategoryDTO> getList(Pageable pageable) {
        try {
            return categoryService.getList(pageable)
                    .map(category -> categoryMapper.convertCategoryToCategoryDTO(category));
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }
}



