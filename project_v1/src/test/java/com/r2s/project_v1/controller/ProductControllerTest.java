package com.r2s.project_v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.CreateProductResponse;
import com.r2s.project_v1.application.dto.response.product.GetProductResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateProductResponse;
import com.r2s.project_v1.application.service.ProductApplicationService;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductApplicationService productService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private CreateProductRequest createProductRequest;
    private UpdateProductRequest updateProductRequest;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("file", "product.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        createProductRequest = new CreateProductRequest();
        createProductRequest.setName("Product 1");
        createProductRequest.setPrice(100.0);
        createProductRequest.setIdCategory(1);
        createProductRequest.setFile(mockFile);

        updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setId(1);
        updateProductRequest.setName("Updated Product");
        updateProductRequest.setPrice(120.0);
        updateProductRequest.setIdCategory(1);
        updateProductRequest.setFile(mockFile);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_shouldReturn201() throws Exception {
        CreateProductResponse createProductResponse = new CreateProductResponse(1, "Product 1", 100.0, "Category 1", "product.jpg");

        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(createProductResponse);

        mockMvc.perform(multipart("/api/v1/product")
                        .file(mockFile)
                        .with(csrf())
                        .param("name", "Product 1")
                        .param("price", "100")
                        .param("idCategory", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.productImage").value("product.jpg"));

        verify(productService, times(1)).createProduct(any(CreateProductRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_shouldReturn200() throws Exception {
        UpdateProductResponse updateProductResponse = new UpdateProductResponse(1, "Updated Product", 120.0, "Category 1", "updated_product.jpg");

        when(productService.updateProduct(any(UpdateProductRequest.class)))
                .thenReturn(updateProductResponse);

        mockMvc.perform(patch("/api/v1/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Product\",\"price\":120.0,\"idCategory\":1,\"file\":\"mockFile\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(120.0))
                .andExpect(jsonPath("$.productImage").value("updated_product.jpg"));

        verify(productService, times(1)).updateProduct(any(UpdateProductRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_shouldReturn200() throws Exception {
        doNothing().when(productService).deleteProduct(anyInt());

        mockMvc.perform(delete("/api/v1/product?id=1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllProducts_shouldReturn200() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        GetProductResponse productResponse = new GetProductResponse(1, "Product 1", 100.0, "Category 1", "product.jpg");

        when(productService.getList(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(productResponse)));

        mockMvc.perform(get("/api/v1/product?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Product 1"))
                .andExpect(jsonPath("$.content[0].price").value(100.0))
                .andExpect(jsonPath("$.content[0].category").value("Category 1"))
                .andExpect(jsonPath("$.content[0].productImage").value("product.jpg"));

        verify(productService, times(1)).getList(pageable);
    }
}
