package com.techlab.ecommerce.config;

import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;

    @ModelAttribute("cartSize")
    public int cartSize(HttpSession session) {
        return cartService.getCartSize();
    }

    @ModelAttribute("loggedUser")
    public Usuario loggedUser(HttpSession session) {
        return (Usuario) session.getAttribute("loggedUser");
    }
}
