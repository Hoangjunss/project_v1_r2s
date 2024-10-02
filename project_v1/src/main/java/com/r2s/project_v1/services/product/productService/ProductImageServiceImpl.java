package com.r2s.project_v1.services.product.productService;

import com.r2s.project_v1.exception.CustomException;
import com.r2s.project_v1.exception.Error;
import com.r2s.project_v1.models.ProductImage;
import com.r2s.project_v1.repository.ProductImageRepository;
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


           return productImageRepository.save(ProductImage.builder()
                    .id(getGenerationId())
                    .imageName(file.getOriginalFilename())
                    .imageType(file.getContentType())
                    .imageData(file.getBytes())
                    .build());


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
