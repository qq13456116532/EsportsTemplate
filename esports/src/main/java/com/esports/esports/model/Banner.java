package com.esports.esports.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;
    
    // Optional: link to a product detail page or other internal page
    private String linkUrl;
}