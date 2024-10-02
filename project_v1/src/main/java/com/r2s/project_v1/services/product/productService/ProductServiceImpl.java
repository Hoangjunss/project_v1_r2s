package com.r2s.project_v1.services.product.productService;

import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.dto.response.product.*;
import com.r2s.project_v1.exception.CustomException;
import com.r2s.project_v1.exception.Error;
import com.r2s.project_v1.models.Category;
import com.r2s.project_v1.models.Product;
import com.r2s.project_v1.repository.ProductRepository;
import com.r2s.project_v1.services.product.categoryService.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Override
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        GetCategoryResponse getCategoryResponse=categoryService.findById(createProductRequest.getIdCategory());

        Category category=modelMapper.map(getCategoryResponse,Category.class);

        if(createProductRequest.getName() == null){
            throw new CustomException(Error.PRODUCT_INVALID_NAME);
        }
        if(createProductRequest.getPrice() == null){
            throw new CustomException(Error.PRODUCT_INVALID_PRICE);
        }
        if(createProductRequest.getFile() == null){//raof

        }

        Product product=Product.builder()
                .id(getGenerationId())
                .name(createProductRequest.getName())
                .price(createProductRequest.getPrice())
                .category(category)
                .productImage(productImageService.save(createProductRequest.getFile()))
                .build();

        try {
            Product productSave=productRepository.save(product);

            CreateProductResponse createProductResponse= modelMapper.map(productSave, CreateProductResponse.class);

            createProductResponse.setCategory(modelMapper.map(productSave.getCategory(),CreateCategoryResponse.class));
            createProductResponse.setFile("http://localhost:8080/api/v1/image?id="+productSave.getProductImage().getId());

            return createProductResponse;

        }  catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.PRODUCT_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }

    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest) {
        GetCategoryResponse getCategoryResponse=categoryService.findById(updateProductRequest.getIdCategory());

        Category category=modelMapper.map(getCategoryResponse,Category.class);

        Product existingProduct=findById(updateProductRequest.getId());

        if(updateProductRequest.getName() == null){
            throw new CustomException(Error.PRODUCT_INVALID_NAME);
        }
        if(updateProductRequest.getPrice() == null){
            throw new CustomException(Error.PRODUCT_INVALID_PRICE);
        }

        existingProduct.setName(updateProductRequest.getName());
        existingProduct.setPrice(updateProductRequest.getPrice());
        existingProduct.setCategory(category);
        if (updateProductRequest.getFile()!=null){
            existingProduct.setProductImage(productImageService.save(updateProductRequest.getFile()));
        }

        try {
            Product productSave=productRepository.save(existingProduct);

            UpdateProductResponse updateProductResponse=modelMapper.map(productSave, UpdateProductResponse.class);

            updateProductResponse.setCategory(modelMapper.map(productSave.getCategory(), UpdateCategoryResponse.class));

            return updateProductResponse;

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
    public Page<GetProductResponse> getList(Pageable pageable) {
        try {
            // Lấy danh sách sản phẩm với phân trang
            Page<Product> productPage = productRepository.findAll(pageable);

            // Ánh xạ mỗi sản phẩm thành GetProductResponse
            return productPage.map(product -> {
                GetProductResponse getProductResponse = modelMapper.map(product, GetProductResponse.class);
                // Ánh xạ category
                getProductResponse.setCategory(modelMapper.map(product.getCategory(), GetCategoryResponse.class));
                return getProductResponse;
            });
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
