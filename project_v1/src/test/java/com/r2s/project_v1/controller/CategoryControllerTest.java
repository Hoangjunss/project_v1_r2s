package com.r2s.project_v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.project_v1.application.dto.product.CategoryCreateDTO;
import com.r2s.project_v1.application.dto.product.CategoryDTO;

import com.r2s.project_v1.application.service.CategoryApplicationService;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.controller.CategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private CategoryApplicationService categoryService;

    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @Value("${server.servlet.context-path}")
    private String prefix;

    private CategoryCreateDTO createCategoryRequest;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        createCategoryRequest = new CategoryCreateDTO();

        createCategoryRequest.setName("Category 1");


        categoryDTO = new CategoryDTO();

        categoryDTO.setId(1);

        categoryDTO.setName("Updated Category");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_shouldReturn201() throws Exception {

        when(categoryService.createCategory(any(CategoryCreateDTO.class)))
                .thenReturn(categoryDTO);

        mockMvc.perform(post(prefix+"category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Category 1\"}"))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryDTO.getId()))
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));

        verify(categoryService, times(1)).createCategory(any(CategoryCreateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_shouldReturn200() throws Exception {


        when(categoryService.updateCategory(any(CategoryDTO.class)))
                .thenReturn(categoryDTO);

        mockMvc.perform(patch(prefix+"/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDTO.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryDTO.getId()))
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));

        verify(categoryService, times(1)).updateCategory(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_shouldReturn200() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyInt());

        mockMvc.perform(delete(prefix+"/category?id=1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllCategories_shouldReturn200() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);



        when(categoryService.getList(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(categoryDTO)));

        mockMvc.perform(get(prefix+"/category?page=0&size=10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(categoryDTO.getId()))
                .andExpect(jsonPath("$.content[0].name").value(categoryDTO.getName()));

        verify(categoryService, times(1)).getList(pageable);
    }

}
