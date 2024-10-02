package com.r2s.project_v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.project_v1.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.services.product.categoryService.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateCategoryRequest createCategoryRequest;
    private UpdateCategoryRequest updateCategoryRequest;
    private CreateCategoryResponse createCategoryResponse;
    private UpdateCategoryResponse updateCategoryResponse;
    private GetCategoryResponse getCategoryResponse;

    @BeforeEach
    void setUp() {
        // Tạo dữ liệu mẫu
        createCategoryRequest = CreateCategoryRequest.builder()
                .name("Sample Category")
                .build();

        updateCategoryRequest = UpdateCategoryRequest.builder()
                .id(1)
                .name("Updated Category Name")
                .build();

        createCategoryResponse = CreateCategoryResponse.builder()
                .id(1)
                .name("Sample Category")
                .build();

        updateCategoryResponse = UpdateCategoryResponse.builder()
                .id(1)
                .name("Updated Category Name")
                .build();

        getCategoryResponse = GetCategoryResponse.builder()
                .id(1)
                .name("Sample Category")
                .build();

        // Mock behavior của CategoryService
        Mockito.when(categoryService.createCategory(Mockito.any(CreateCategoryRequest.class)))
                .thenReturn(createCategoryResponse);

        Mockito.when(categoryService.updateCategory(Mockito.any(UpdateCategoryRequest.class)))
                .thenReturn(updateCategoryResponse);

        Mockito.when(categoryService.getList())
                .thenReturn(Collections.singletonList(getCategoryResponse));

        Mockito.doNothing().when(categoryService).deleteCategory(Mockito.anyInt());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_shouldReturnCreatedStatus() throws Exception {
        mockMvc.perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(patch("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/api/v1/category")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllCategories_shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
