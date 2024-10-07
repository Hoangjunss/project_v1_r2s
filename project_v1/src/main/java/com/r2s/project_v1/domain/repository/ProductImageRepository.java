package com.r2s.project_v1.domain.repository;

import com.r2s.project_v1.domain.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
}
