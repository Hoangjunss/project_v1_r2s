package com.r2s.project_v1.infrastructure.service;

import com.r2s.project_v1.application.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.application.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.application.dto.response.product.*;
import com.r2s.project_v1.domain.models.Category;
import com.r2s.project_v1.domain.models.Product;
import com.r2s.project_v1.domain.repository.ProductRepository;
import com.r2s.project_v1.domain.service.CategoryService;
import com.r2s.project_v1.domain.service.ProductImageService;
import com.r2s.project_v1.domain.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Override
    public Product createProduct(Product product) {




        if(product.getName() == null){
            throw new CustomException(Error.PRODUCT_INVALID_NAME);
        }
        if(product.getPrice() == null){
            throw new CustomException(Error.PRODUCT_INVALID_PRICE);
        }
        if(product.getProductImage() == null){//raof

        }

       product.setId(getGenerationId());

        try {

            return productRepository.save(product);

        }  catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.PRODUCT_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }

    }

    @Override
    public Product updateProduct(Product product) {


        Product existingProduct=findById(product.getId());

        if(product.getName() == null){
            throw new CustomException(Error.PRODUCT_INVALID_NAME);
        }
        if(product.getPrice() == null){
            throw new CustomException(Error.PRODUCT_INVALID_PRICE);
        }

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        if (product.getProductImage()!=null){
            existingProduct.setProductImage(product.getProductImage());
        }

        try {

            return productRepository.save(existingProduct);

        } catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.PRODUCT_UNABLE_TO_UPDATE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public void deleteProduct(Integer id) {

        Product existingProduct=findById(id);

        try {
            productRepository.delete(existingProduct);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.PRODUCT_UNABLE_TO_DELETE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }


    @Override
    public Page<Product> getList(Pageable pageable) {
        try {

            return productRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }
    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.PRODUCT_NOT_FOUND));
    }
}