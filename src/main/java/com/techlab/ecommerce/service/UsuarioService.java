package com.techlab.ecommerce.service;

import com.techlab.ecommerce.entity.Usuario;
import com.techlab.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository userRepo;

    public UsuarioService(UsuarioRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Usuario findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public Usuario save(Usuario user) {
        return userRepo.save(user);
    }

    public boolean authenticate(String email, String rawPassword) {
        Usuario u = findByEmail(email);
        return u != null && u.getPassword().equals(rawPassword);
    }
}
