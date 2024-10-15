package com.r2s.project_v1.infrastructure.service;


import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.repository.CategoryRepository;
import com.r2s.project_v1.domain.service.CategoryService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Category createCategory(Category category) {
        if (category.getName() == null) {
            throw new CustomException(Error.CATEGORY_INVALID_NAME);
        }

        category.setId(getGenerationId());

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        if (category.getName() == null) {
            throw new CustomException(Error.CATEGORY_INVALID_NAME);
        }

        findById(category.getId());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {

        Category category = findID(id);

        categoryRepository.delete(category);
    }

    @Override
    public Category findById(Integer id) {
        return findID(id);
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }

    public Category findID(Integer id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(Error.CATEGORY_NOT_FOUND));
    }

    @Override
    public Page<Category> getList(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}