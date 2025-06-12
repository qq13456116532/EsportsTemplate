package com.esports.esports.controller;

import com.esports.esports.model.Banner;
import com.esports.esports.model.Category;
import com.esports.esports.model.Product;
import com.esports.esports.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/banners")
    public ResponseEntity<List<Banner>> getBanners() {
        return ResponseEntity.ok(shopService.getBanners());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(shopService.getAllCategories());
    }

    @GetMapping("/products/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        return ResponseEntity.ok(shopService.getFeaturedProducts());
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String searchQuery) {
        if (categoryId != null) {
            return ResponseEntity.ok(shopService.getProductsByCategoryId(categoryId));
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            return ResponseEntity.ok(shopService.searchProducts(searchQuery));
        }
        // Default to featured if no params
        return ResponseEntity.ok(shopService.getFeaturedProducts());
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return shopService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}