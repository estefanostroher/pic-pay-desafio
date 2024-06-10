package com.example.picpay.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.picpay.Application;
import com.example.picpay.dto.DadosListagemUsuarioDTO;
import com.example.picpay.repository.UsuarioRepository;
import com.example.picpay.service.UsuarioService;
import com.example.picpay.usuario.TipoDeUsuario;
import com.example.picpay.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@exemplo.com");
        usuario.setCpf("12345678901");
        usuario.setNomeCompleto("Nome Completo");
        usuario.setSaldo(new BigDecimal("100.00"));
        usuario.setTipoDeUsuario(TipoDeUsuario.COMUM);
        usuario.setAtivo(true);
    }

    @Test
    public void testFindUsuarioById() {
        when(usuarioRepository.findUsuarioById(1L)).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.findUsuarioById(1L);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void testFindUsuarioByIdException() {
        when(usuarioRepository.findUsuarioById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.findUsuarioById(1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Usário não encontrado pelo ID informado no método \"findUsuarioById\" na classe \"UsuarioService\".");
    }

    @Test
    public void testFindAllByAtivoTrue() {
        when(usuarioRepository.findAllByAtivoTrue()).thenReturn(List.of(usuario));
        List<DadosListagemUsuarioDTO> usuarios = usuarioService.findAllByAtivoTrue();
        assertThat(usuarios).isNotEmpty();
        assertThat(usuarios.get(0).id()).isEqualTo(1L);
    }

    @Test
    public void testFindAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.findAllUsuarios();
        assertThat(usuarios).isNotEmpty();
        assertThat(usuarios.get(0).getId()).isEqualTo(1L);
    }

    @Test
    public void testSalvarUsuario() {
        usuarioService.salvarUsuario(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void testCadastrarUsuario() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto("João Pedro");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);
        assertThat(novoUsuario).isNotNull();
    }

    @Test
    public void testFindUsuarioByEmail() {
        when(usuarioRepository.findUsuarioByEmail("teste@exemplo.com")).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.findUsuarioByEmail("teste@exemplo.com");
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("teste@exemplo.com");
    }

    @Test
    public void testFindUsuarioByEmailException() {
        when(usuarioRepository.findUsuarioByEmail("teste@exemplo.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.findUsuarioByEmail("teste@exemplo.com"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Usuário não encontrado pelo email informado no método \"findUsuarioByEmail\" na classe \"UsuarioService\".");
    }

    @Test
    public void testFindUsuarioByNomeCompleto() {
        when(usuarioRepository.findUsuarioByNomeCompleto("Nome Completo")).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.findUsuarioByNomeCompleto("Nome Completo");
        assertThat(found).isNotNull();
        assertThat(found.getNomeCompleto()).isEqualTo("Nome Completo");
    }

    @Test
    public void testFindUsuarioByNomeCompletoException() {
        when(usuarioRepository.findUsuarioByNomeCompleto("Nome Completo")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.findUsuarioByNomeCompleto("Nome Completo"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Usuário não encontrado pelo nome completo informado no método \"findUsuarioByNomeCompleto\" na classe \"UsuarioService\".");
    }

    @Test
    public void testFindUsuarioByCpf() {
        when(usuarioRepository.findUsuarioByCpf("12345678901")).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.findUsuarioByCpf("12345678901");
        assertThat(found).isNotNull();
        assertThat(found.getCpf()).isEqualTo("12345678901");
    }

    @Test
    public void testFindUsuarioByCpfException() {
        when(usuarioRepository.findUsuarioByCpf("12345678901")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.findUsuarioByCpf("12345678901"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Usuário não encontrado pelo CPF informado no método \"findUsuarioByCpf\" na classe \"UsuarioService\".");
    }

    @Test
    public void testAtualizarUsuario() {
        usuario.setNomeCompleto("nome atualizado");
        assertThat(usuario.getNomeCompleto()).isEqualTo("nome atualizado");
    }

    @Test
    public void testDeletarUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        this.usuarioRepository.deleteById(usuario.getId());
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
