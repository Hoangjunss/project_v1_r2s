package com.r2s.project_v1.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.r2s.project_v1.application.dto.product.ProductCreateDTO;
import com.r2s.project_v1.application.dto.product.ProductDTO;
import com.r2s.project_v1.application.dto.product.ProductUpdateDTO;

import com.r2s.project_v1.application.service.ProductApplicationService;
import com.r2s.project_v1.infrastructure.security.JwtTokenUtil;
import com.r2s.project_v1.infrastructure.security.OurUserDetailsService;
import com.r2s.project_v1.presentation.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${server.servlet.context-path}")
    private String prefix;

    @MockBean
    private ProductApplicationService productService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private ProductCreateDTO createProductRequest;
    private ProductUpdateDTO updateProductRequest;
    private ProductDTO productDTO;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("file", "product.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        createProductRequest = new ProductCreateDTO();

        createProductRequest.setName("Product 1");

        createProductRequest.setPrice(100.0);

        createProductRequest.setIdCategory(1);

        createProductRequest.setFile(mockFile);

        updateProductRequest = new ProductUpdateDTO();

        updateProductRequest.setId(1);

        updateProductRequest.setName("Updated Product");

        updateProductRequest.setPrice(120.0);

        updateProductRequest.setIdCategory(1);

        updateProductRequest.setFile(mockFile);

        productDTO = new ProductDTO();

        productDTO.setId(1);

        productDTO.setName("Updated Product");

        productDTO.setPrice(120.0);

        productDTO.setCategory("brand");
        productDTO.setProductImage("/image");

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_shouldReturn201() throws Exception {


        when(productService.createProduct(any(ProductCreateDTO.class)))
                .thenReturn(productDTO);

        mockMvc.perform(multipart(prefix+"/product")
                        .file(mockFile)
                        .with(csrf())
                        .param("name", createProductRequest.getName())
                        .param("price", createProductRequest.getPrice().toString())
                        .param("idCategory", createProductRequest.getIdCategory().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.price").value(productDTO.getPrice()))
                .andExpect(jsonPath("$.productImage").value(productDTO.getProductImage()));

        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_shouldReturn200() throws Exception {


        when(productService.updateProduct(any(ProductUpdateDTO.class)))
                .thenReturn(productDTO);

        mockMvc.perform(patch(prefix+"/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProductRequest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.price").value(productDTO.getPrice()))
                .andExpect(jsonPath("$.productImage").value(productDTO.getProductImage()));

        verify(productService, times(1)).updateProduct(any(ProductUpdateDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_shouldReturn200() throws Exception {
        doNothing().when(productService).deleteProduct(anyInt());

        mockMvc.perform(delete(prefix+"/product?id=1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(anyInt());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllProducts_shouldReturn200() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);

        when(productService.getList(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(productDTO)));

        mockMvc.perform(get(prefix+"/product?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(productDTO.getId()))
                .andExpect(jsonPath("$.content[0].name").value(productDTO.getName()))
                .andExpect(jsonPath("$.content[0].price").value(productDTO.getPrice()))
                .andExpect(jsonPath("$.content[0].category").value(productDTO.getCategory()))
                .andExpect(jsonPath("$.content[0].productImage").value(productDTO.getProductImage()));

        verify(productService, times(1)).getList(pageable);
    }
}
