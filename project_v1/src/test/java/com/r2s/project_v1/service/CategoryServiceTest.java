package com.r2s.project_v1.service;

import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.repository.CategoryRepository;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.infrastructure.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1);
        category.setName("Test Category");
    }

    // Test createCategory method
    @Test
    void createCategory_ShouldReturnSavedCategory_WhenValid() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category savedCategory = categoryService.createCategory(category);
        assertNotNull(savedCategory);
        assertEquals("Test Category", savedCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_ShouldThrowException_WhenCategoryNameIsNull() {
        category.setName(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            categoryService.createCategory(category);
        });
        assertEquals(Error.CATEGORY_INVALID_NAME, exception.getError());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void createCategory_ShouldThrowException_WhenSaveFails() {
        when(categoryRepository.save(any(Category.class))).thenThrow(DataIntegrityViolationException.class);
        CustomException exception = assertThrows(CustomException.class, () -> {
            categoryService.createCategory(category);
        });
        assertEquals(Error.CATEGORY_UNABLE_TO_SAVE, exception.getError());
    }

    // Test updateCategory method
    @Test
    void updateCategory_ShouldReturnUpdatedCategory_WhenValid() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category updatedCategory = categoryService.updateCategory(category);
        assertNotNull(updatedCategory);
        assertEquals("Test Category", updatedCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNameIsNull() {
        category.setName(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            categoryService.updateCategory(category);
        });
        assertEquals(Error.CATEGORY_INVALID_NAME, exception.getError());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // Test deleteCategory method
    @Test
    void deleteCategory_ShouldDeleteCategory_WhenValid() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));
        assertDoesNotThrow(() -> categoryService.deleteCategory(1));
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            categoryService.deleteCategory(1);
        });
        assertEquals(Error.CATEGORY_NOT_FOUND, exception.getError());
    }

    // Test getList method
    @Test
    void getList_ShouldReturnPageOfCategories_WhenValid() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Page<Category> result = categoryService.getList(pageable);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findAll(pageable);
    }
}
