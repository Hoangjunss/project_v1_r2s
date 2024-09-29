package com.r2s.project_v1.services.product.categoryService;

import com.r2s.project_v1.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.models.Category;
import com.r2s.project_v1.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {

        Category category=Category.builder()
                .id(getGenerationId())
                .name(createCategoryRequest.getName())
                .build();
        Category categorySave=categoryRepository.save(category);

        return modelMapper.map(categorySave,CreateCategoryResponse.class);
    }

    @Override
    public UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        GetCategoryResponse getCategoryResponse=findById(updateCategoryRequest.getId());

        Category category=Category.builder()
                .id(getCategoryResponse.getId())
                .name(updateCategoryRequest.getName())
                .build();

        Category categorySave=categoryRepository.save(category);

        return modelMapper.map(categorySave, UpdateCategoryResponse.class);
    }

    @Override
    public void deleteCategory(Integer id) {

        Category category=findID(id);

       categoryRepository.delete(category);


    }

    @Override
    public List<GetCategoryResponse> getList() {

        List<Category> categoryList=categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> modelMapper.map(category, GetCategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public GetCategoryResponse findById(Integer id) {


        return modelMapper.map(findID(id), GetCategoryResponse.class);
    }
    public Integer getGenerationId() {

        UUID uuid = UUID.randomUUID();
        // Use most significant bits and ensure it's within the integer range
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
    public Category findID(Integer id) {
        return categoryRepository.findById(id).orElseThrow();
    }
}
