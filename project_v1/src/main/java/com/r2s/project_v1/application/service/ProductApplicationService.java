package com.r2s.project_v1.application.service;

import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.*;
import com.r2s.project_v1.domain.models.ProductImage;
import com.r2s.project_v1.domain.service.ProductImageService;
import com.r2s.project_v1.domain.service.ProductService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.models.Product;
import com.r2s.project_v1.domain.repository.ProductRepository;
import com.r2s.project_v1.domain.service.CategoryService;
import com.r2s.project_v1.presentation.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Slf4j
@Service
public class ProductApplicationService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;

    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        log.info("Category {}",createProductRequest.toString());
        Category category=categoryService.findById(createProductRequest.getIdCategory());
      log.info("Category {}",category.toString());
        ProductImage productImage=productImageService.save(createProductRequest.getFile());
        log.info("productImage {}",productImage.toString());
        Product product=productMapper.convertCreateProductRequestToProduct(createProductRequest,category,productImage);
        log.info("product {}",product.toString());
         Product productSave=productService.createProduct(product);
        log.info("productSave {}",productSave.toString());
         return productMapper.convertProductToCreateProductResponse(productSave);
    }


    public UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest) {

       Category category=categoryService.findById(updateProductRequest.getIdCategory());

       ProductImage productImage=updateProductRequest.getFile()!=null?productImageService.save(updateProductRequest.getFile()):null;

       Product product=productMapper.convertUpdateProductRequestToProduct(updateProductRequest,category,productImage);

       Product productUpdate=productService.updateProduct(product);

       return productMapper.convertProductToUpdateProductResponse(productUpdate);
    }


    public void deleteProduct(Integer id) {

       productService.deleteProduct(id);
    }

    public Page<GetProductResponse> getList(Pageable pageable) {
        try {

           return productService.getList(pageable).map(product -> productMapper.convertProductToGetProductResponse(product));

        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }

}
