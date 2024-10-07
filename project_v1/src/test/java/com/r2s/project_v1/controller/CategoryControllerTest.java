package com.r2s.project_v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.project_v1.application.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.application.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.application.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.application.service.CategoryApplicationServiceImpl;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.controller.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.Collections;




@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryApplicationServiceImpl categoryService;

    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private CreateCategoryRequest createCategoryRequest;
    private UpdateCategoryRequest updateCategoryRequest;

    @BeforeEach
    void setUp() {
        createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setName("Category 1");

        updateCategoryRequest = new UpdateCategoryRequest();
        updateCategoryRequest.setId(1);
        updateCategoryRequest.setName("Updated Category");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_shouldReturn201() throws Exception {
        // Giả lập kết quả trả về của service
        when(categoryService.createCategory(any(CreateCategoryRequest.class)))
                .thenReturn(new CreateCategoryResponse(1, "Category 1"));

        mockMvc.perform(post("/api/v1/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Category 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));

        verify(categoryService, times(1)).createCategory(any(CreateCategoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_shouldReturn200() throws Exception {
        UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse(1, "Updated Category");

        when(categoryService.updateCategory(any(UpdateCategoryRequest.class)))
                .thenReturn(updateCategoryResponse);

        mockMvc.perform(patch("/api/v1/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category"));

        verify(categoryService, times(1)).updateCategory(any(UpdateCategoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_shouldReturn200() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyInt());

        mockMvc.perform(delete("/api/v1/category?id=1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllCategories_shouldReturn200() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        GetCategoryResponse categoryResponse = new GetCategoryResponse(1, "Category 1");
        when(categoryService.getList(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(categoryResponse)));

        mockMvc.perform(get("/api/v1/category?page=0&size=10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Category 1"));

        verify(categoryService, times(1)).getList(pageable);
    }

}
