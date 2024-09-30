package com.r2s.project_v1.repository;

import com.r2s.project_v1.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
