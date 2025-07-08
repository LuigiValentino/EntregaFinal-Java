package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Product;
import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Product> allProducts = productService.getAllProducts();

        List<Product> featuredProducts = allProducts.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.shuffle(list);
                            return list.stream().limit(3).collect(Collectors.toList());
                        }
                ));

        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("products", allProducts);

        Usuario loggedUser = (Usuario) session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", loggedUser);

        List<?> cart = (List<?>) session.getAttribute("cart");
        model.addAttribute("cartSize", cart != null ? cart.size() : 0);

        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
