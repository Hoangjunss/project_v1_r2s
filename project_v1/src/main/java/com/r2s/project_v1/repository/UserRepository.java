package com.r2s.project_v1.repository;



import com.r2s.project_v1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
   Optional<User> findByEmail(String email);
   Optional<User> findById(Integer id);
   List<User> findAll();
   void deleteById(Integer id);

}
