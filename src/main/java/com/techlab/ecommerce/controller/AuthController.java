package com.techlab.ecommerce.controller;

import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Usuario user, Model model) {
        if (usuarioService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El email ya está registrado");
            return "auth/register";
        }
        usuarioService.save(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") Usuario user, Model model, HttpSession session) {
        if (usuarioService.authenticate(user.getEmail(), user.getPassword())) {
            Usuario u = usuarioService.findByEmail(user.getEmail());
            session.setAttribute("loggedUser", u);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
