package com.techlab.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Question question;

    @Column(length = 1000)
    private String answerText;

    private LocalDateTime createdAt = LocalDateTime.now();
}
