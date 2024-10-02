package com.r2s.project_v1.domain.repository;

import com.r2s.project_v1.domain.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
