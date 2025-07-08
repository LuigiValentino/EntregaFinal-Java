package com.techlab.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.ecommerce.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
    
