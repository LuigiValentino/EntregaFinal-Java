package com.techlab.ecommerce.service;

import com.techlab.ecommerce.entity.Product;
import com.techlab.ecommerce.repository.OrderItemRepository;
import com.techlab.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, 
                         OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void reduceStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setStock(product.getStock() - quantity);
            saveProduct(product);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        orderItemRepository.deleteByProductId(id);
        
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getFilteredProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<String> getUniqueCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}