package com.r2s.project_v1.services.product.categoryService;

import com.r2s.project_v1.dto.request.product.CreateCategoryRequest;
import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.dto.request.product.UpdateCategoryRequest;
import com.r2s.project_v1.dto.response.product.CreateCategoryResponse;
import com.r2s.project_v1.dto.response.product.GetCategoryResponse;
import com.r2s.project_v1.dto.response.product.UpdateCategoryResponse;
import com.r2s.project_v1.exception.CustomException;
import com.r2s.project_v1.exception.Error;
import com.r2s.project_v1.models.Category;
import com.r2s.project_v1.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {

        if(createCategoryRequest.getName()==null){
            throw new CustomException(Error.CATEGORY_INVALID_NAME);
        }

        Category category=Category.builder()
                .id(getGenerationId())
                .name(createCategoryRequest.getName())
                .build();

        try {
            Category categorySave=categoryRepository.save(category);

            return modelMapper.map(categorySave,CreateCategoryResponse.class);
        }
        catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.CATEGORY_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        GetCategoryResponse getCategoryResponse=findById(updateCategoryRequest.getId());

        if (updateCategoryRequest.getName() == null){
            throw new CustomException(Error.CATEGORY_INVALID_NAME);
        }

        Category category=Category.builder()
                .id(getCategoryResponse.getId())
                .name(updateCategoryRequest.getName())
                .build();

        try {
            Category categorySave=categoryRepository.save(category);

            return modelMapper.map(categorySave, UpdateCategoryResponse.class);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.CATEGORY_UNABLE_TO_UPDATE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }

    }

    @Override
    public void deleteCategory(Integer id) {

        Category category=findID(id);

        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(Error.CATEGORY_UNABLE_TO_DELETE);
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }

    }

    @Override
    public List<GetCategoryResponse> getList() {

        try {
            List<Category> categoryList=categoryRepository.findAll();

            return categoryList.stream()
                    .map(category -> modelMapper.map(category, GetCategoryResponse.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public GetCategoryResponse findById(Integer id) {
        return modelMapper.map(findID(id), GetCategoryResponse.class);
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }

    public Category findID(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.CATEGORY_NOT_FOUND));
    }
}
