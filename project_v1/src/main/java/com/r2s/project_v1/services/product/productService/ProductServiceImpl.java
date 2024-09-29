package com.r2s.project_v1.services.product.productService;

import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateProductRequest;
import com.r2s.project_v1.dto.response.product.*;
import com.r2s.project_v1.models.Category;
import com.r2s.project_v1.models.Product;
import com.r2s.project_v1.repository.ProductRepository;
import com.r2s.project_v1.services.product.categoryService.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    @Override
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        GetCategoryResponse getCategoryResponse=categoryService.findById(createProductRequest.getIdCategory());

        Category category=modelMapper.map(getCategoryResponse,Category.class);

        Product product=Product.builder()
                .id(getGenerationId())
                .name(createProductRequest.getName())
                .price(createProductRequest.getPrice())
                .category(category).build();

        Product productSave=productRepository.save(product);

       CreateProductResponse createProductResponse= modelMapper.map(productSave, CreateProductResponse.class);

       createProductResponse.setCategory(modelMapper.map(productSave.getCategory(),CreateCategoryResponse.class));

        return createProductResponse;

    }

    @Override
    public UpdateProductResponse updateProduct(UpdateProductRequest updateProductRequest) {
        GetCategoryResponse getCategoryResponse=categoryService.findById(updateProductRequest.getIdCategory());

        Category category=modelMapper.map(getCategoryResponse,Category.class);

        Product existingProduct=findById(updateProductRequest.getId());

        Product updatedProduct = Product.builder()
                .id(existingProduct.getId())
                .price(updateProductRequest.getPrice())
                .name(updateProductRequest.getName())
                .category(category)
                .build();

        Product productSave=productRepository.save(updatedProduct);

        UpdateProductResponse updateProductResponse=modelMapper.map(productSave, UpdateProductResponse.class);

        updateProductResponse.setCategory(modelMapper.map(productSave.getCategory(), UpdateCategoryResponse.class));

        return updateProductResponse;
    }

    @Override
    public void deleteProduct(Integer id) {

        Product existingProduct=findById(id);

        productRepository.delete(existingProduct);
    }

    @Override
    public List<GetProductResponse> getList() {

        List<Product> productList=productRepository.findAll();

        return productList.stream()
                .map(product -> {
                    GetProductResponse getProductResponse=modelMapper.map(product, GetProductResponse.class);

                    getProductResponse.setCategory(modelMapper.map(product.getCategory(), GetCategoryResponse.class));

                    return getProductResponse;
                })
                .collect(Collectors.toList());
    }
    public Integer getGenerationId() {

        UUID uuid = UUID.randomUUID();
        // Use most significant bits and ensure it's within the integer range
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
    public Product findById(Integer id) {

        return productRepository.findById(id).orElseThrow();
    }
}
