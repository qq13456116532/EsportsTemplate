package com.esports.esports.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer sales;
    private Integer views;
    private String imageUrl;
    private String description;

    // @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "category_id")
    private Category category;
}