package com.esports.esports.service;

import com.esports.esports.model.Banner;
import com.esports.esports.model.Category;
import com.esports.esports.model.Product;
import com.esports.esports.repository.BannerRepository;
import com.esports.esports.repository.CategoryRepository;
import com.esports.esports.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BannerRepository bannerRepository;

    public List<Banner> getBanners() {
        return bannerRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    public List<Product> getFeaturedProducts() {
        // For now, we'll just return the first few products as "featured"
        // A real implementation might have a "featured" flag on the product
        return productRepository.findAll().stream().limit(3).toList();
    }

    public Optional<Product> getProductById(Long id) {
        // A simple example of incrementing views on fetch
        productRepository.findById(id).ifPresent(product -> {
            product.setViews(product.getViews() + 1);
            productRepository.save(product);
        });
        return productRepository.findById(id);
    }
     
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}