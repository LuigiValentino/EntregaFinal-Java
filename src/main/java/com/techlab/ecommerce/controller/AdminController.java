package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Product;
import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.OrderService;
import com.techlab.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    private boolean isAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("loggedUser");
        return u != null && "ADMIN".equals(u.getRole());
    }

    @GetMapping
    public String redirectAdminRoot() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping({"/dashboard", "/"})
    public String dashboard(@RequestParam(value = "tab", required = false, defaultValue = "products") String tab,
                            Model model,
                            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("activeTab", tab);
        
        if (model.containsAttribute("success")) {
            model.addAttribute("success", model.getAttribute("success"));
        }
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }

        return "admin/dashboard";
    }

    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute Product product, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear producto: " + e.getMessage());
        }
        return "redirect:/admin/dashboard?tab=products";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, 
                             @ModelAttribute Product product, 
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        try {
            product.setId(id);
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
        }
        return "redirect:/admin/dashboard?tab=products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/admin/dashboard?tab=products";
    }

    @PostMapping("/orders/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id, 
                                   @RequestParam String status, 
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar estado: " + e.getMessage());
        }
        return "redirect:/admin/dashboard?tab=orders";
    }
}