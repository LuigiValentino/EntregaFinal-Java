package com.techlab.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private String userName;

    @Column(length = 1000)
    private String questionText;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;
}
