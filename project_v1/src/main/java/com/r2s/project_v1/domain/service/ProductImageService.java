package com.r2s.project_v1.domain.service;

import com.r2s.project_v1.domain.models.ProductImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface ProductImageService {
    ProductImage save(MultipartFile file);
    ProductImage get(Integer id);
}
