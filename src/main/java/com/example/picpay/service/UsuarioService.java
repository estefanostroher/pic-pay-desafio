package com.example.picpay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.picpay.dto.DadosAtualizarUsuarioDTO;
import com.example.picpay.dto.DadosCadastroUsuarioDTO;
import com.example.picpay.dto.DadosListagemUsuarioDTO;
import com.example.picpay.repository.UsuarioRepository;
import com.example.picpay.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findUsuarioById(Long id) {
        return this.usuarioRepository.findUsuarioById(id).orElseThrow(()-> new EntityNotFoundException("Usário não encontrado pelo ID informado no método \"findUsuarioById\" na classe \"UsuarioService\"."));
    }

    public List<DadosListagemUsuarioDTO> findAllByAtivoTrue() {
        var lista = this.usuarioRepository.findAllByAtivoTrue();
        return lista.stream().map(DadosListagemUsuarioDTO::new).toList();
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public void salvarUsuario(Usuario usuario) {
        this.usuarioRepository.save(usuario);
    }

    public Usuario cadastrarUsuario(DadosCadastroUsuarioDTO dados) {
        var usuario = new Usuario(dados);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findUsuarioByEmail(email).orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado pelo email informado no método \"findUsuarioByEmail\" na classe \"UsuarioService\"."));
    }

    public Usuario findUsuarioByNomeCompleto(String nomeCompleto) {
        return this.usuarioRepository.findUsuarioByNomeCompleto(nomeCompleto).orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado pelo nome completo informado no método \"findUsuarioByNomeCompleto\" na classe \"UsuarioService\"."));
    }

    public Usuario findUsuarioByCpf(String cpf) {
        return this.usuarioRepository.findUsuarioByCpf(cpf).orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado pelo CPF informado no método \"findUsuarioByCpf\" na classe \"UsuarioService\"."));
    }

    public Usuario atualizarUsuarioById(DadosAtualizarUsuarioDTO dados) {
        var usuario = this.usuarioRepository.findById(dados.id()).orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado pelo ID informado no método \"atualizarUsuario\" na classe \"UsuarioService\"."));
        usuario.atualizarUsuario(dados);
        return usuario;
    }

    public void deletarUsuarioById(Long id) {
        var usuario = this.usuarioRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Usário não encontrado pelo ID informado no método método \"deletarUsuario\" na classe \"UsuarioService\"."));
        this.usuarioRepository.delete(usuario);
    }
}
