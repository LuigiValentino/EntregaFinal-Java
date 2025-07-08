package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Product;
import com.techlab.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;

    public ShopController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public String shopPage() {
        return "shop";
    }

    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> apiFilteredProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return productService.getFilteredProducts(name, category, minPrice, maxPrice);
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public List<String> apiCategories() {
        return productService.getUniqueCategories();
    }
}
