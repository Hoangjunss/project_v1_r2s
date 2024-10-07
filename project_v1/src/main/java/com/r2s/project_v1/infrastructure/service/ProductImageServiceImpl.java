package com.r2s.project_v1.infrastructure.service;

import com.r2s.project_v1.domain.models.ProductImage;
import com.r2s.project_v1.domain.repository.ProductImageRepository;
import com.r2s.project_v1.domain.service.ProductImageService;
import com.r2s.project_v1.infrastructure.exception.CustomException;
import com.r2s.project_v1.infrastructure.exception.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ProductImage save(MultipartFile file) {
        try {
            ProductImage productImage=ProductImage.builder()
                    .id(getGenerationId())
                    .imageName(file.getOriginalFilename())
                    .imageType(file.getContentType())
                    .imageData(file.getBytes())
                    .build();
             productImage.setUrl("http://localhost:8080/api/image?id="+productImage.getId());

            return productImageRepository.save(productImage);


        } catch (Exception e) {
            throw new CustomException(Error.DATABASE_ACCESS_ERROR);
        }

    }

    @Override
    public ProductImage get(Integer id) {

        return productImageRepository.findById(id).orElseThrow();

    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}