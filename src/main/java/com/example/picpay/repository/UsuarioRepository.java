package com.example.picpay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.picpay.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUsuarioById(Long id);
    Optional<Usuario> findUsuarioByEmail(String email);
    Optional<Usuario> findUsuarioByCpf(String cpf);
    Optional<Usuario> findUsuarioByNomeCompleto(String nomeCompleto);
    List<Usuario> findAllByAtivoTrue();
}
