package com.r2s.project_v1.service;

import com.r2s.project_v1.domain.models.Product;
import com.r2s.project_v1.domain.repository.ProductRepository;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.infrastructure.service.ProductServiceImpl;
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

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(100.0);
    }

    // Test createProduct method
    @Test
    void createProduct_ShouldReturnSavedProduct_WhenValid() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product savedProduct = productService.createProduct(product);
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenProductNameIsNull() {
        product.setName(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.createProduct(product);
        });
        assertEquals(Error.PRODUCT_INVALID_NAME, exception.getError());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenProductPriceIsNull() {
        product.setPrice(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.createProduct(product);
        });
        assertEquals(Error.PRODUCT_INVALID_PRICE, exception.getError());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrowException_WhenSaveFails() {
        when(productRepository.save(any(Product.class))).thenThrow(DataIntegrityViolationException.class);
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.createProduct(product);
        });
        assertEquals(Error.PRODUCT_UNABLE_TO_SAVE, exception.getError());
    }

    // Test updateProduct method
    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenValid() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product updatedProduct = productService.updateProduct(product);
        assertNotNull(updatedProduct);
        assertEquals("Test Product", updatedProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNameIsNull() {
        product.setName(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.updateProduct(product);
        });
        assertEquals(Error.PRODUCT_INVALID_NAME, exception.getError());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductPriceIsNull() {
        product.setPrice(null);
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.updateProduct(product);
        });
        assertEquals(Error.PRODUCT_INVALID_PRICE, exception.getError());
        verify(productRepository, never()).save(any(Product.class));
    }

    // Test deleteProduct method
    @Test
    void deleteProduct_ShouldDeleteProduct_WhenValid() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));
        assertDoesNotThrow(() -> productService.deleteProduct(1));
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
        CustomException exception = assertThrows(CustomException.class, () -> {
            productService.deleteProduct(1);
        });
        assertEquals(Error.PRODUCT_NOT_FOUND, exception.getError());
    }

    // Test getList method
    @Test
    void getList_ShouldReturnPageOfProducts_WhenValid() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        Page<Product> result = productService.getList(pageable);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }
}