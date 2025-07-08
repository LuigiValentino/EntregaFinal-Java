package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioService userService;

    public LoginController(UsuarioService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session) {
        if (userService.authenticate(email, password)) {
            Usuario u = userService.findByEmail(email);
            session.setAttribute("loggedUser", u);
            return u.getRole().equals("ADMIN")
                   ? "redirect:/admin/products"
                   : "redirect:/shop";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
