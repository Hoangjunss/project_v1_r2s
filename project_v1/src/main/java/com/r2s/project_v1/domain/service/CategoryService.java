package com.r2s.project_v1.domain.service;


import com.r2s.project_v1.domain.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Integer id);
    Page<Category> getList(Pageable pageable);
    Category findById(Integer id);
}
