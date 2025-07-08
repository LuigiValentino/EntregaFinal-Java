package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.CartService;
import com.techlab.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        model.addAttribute("cartSize", cartService.getCartSize());
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Producto añadido al carrito!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shop";
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotalAmount());
        model.addAttribute("loggedUser", user);
        return "cart/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(HttpSession session,
                                  @RequestParam("shippingAddress") String shippingAddress,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("loggedUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para continuar");
            return "redirect:/auth/login";
        }

        try {
            orderService.createOrder(user, cartService.getCartItems(), shippingAddress, paymentMethod);
            cartService.clearCart();
            redirectAttributes.addFlashAttribute("success", "¡Compra realizada con éxito!");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la orden: " + e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}
