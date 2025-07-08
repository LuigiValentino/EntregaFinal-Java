package com.techlab.ecommerce.repository;

import com.techlab.ecommerce.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByProductId(Long productId);
}
