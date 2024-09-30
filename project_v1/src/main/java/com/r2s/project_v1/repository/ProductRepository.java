package com.r2s.project_v1.repository;

import com.r2s.project_v1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
