package com.example.picpay.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import com.example.picpay.dto.DadosCadastroTransacaoDTO;
import com.example.picpay.infra.SaldoInsuficienteException;
import com.example.picpay.infra.TransacaoEntreMesmosUsuariosException;
import com.example.picpay.infra.UsuarioNaoAutorizadoException;
import com.example.picpay.repository.TransacaoRepository;
import com.example.picpay.service.TransacaoService;
import com.example.picpay.service.UsuarioService;
import com.example.picpay.transacao.Transacao;
import com.example.picpay.usuario.TipoDeUsuario;
import com.example.picpay.usuario.Usuario;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class TransacaoServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private Transacao transacao;

    @Mock
    private Usuario remetente;

    @Mock
    private Usuario destinatario;

    @Mock
    private DadosCadastroTransacaoDTO transacaoDTO;

    @BeforeEach
    public void setUp() {
        remetente = new Usuario();
        remetente.setId(1L);
        remetente.setSaldo(BigDecimal.valueOf(100));
        remetente.setTipoDeUsuario(TipoDeUsuario.COMUM);

        destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setSaldo(BigDecimal.valueOf(50));

        transacaoDTO = new DadosCadastroTransacaoDTO(1L, 2L, BigDecimal.TEN);

        transacao = new Transacao();
        transacao.setId(1L);
        transacao.setRemetenteId(remetente);
        transacao.setDestinatarioId(destinatario);
        transacao.setValor(BigDecimal.TEN);
        transacao.setAtivo(true);
    }

    @Test
    public void testSaldoInsuficienteException() {
        remetente.setSaldo(BigDecimal.valueOf(5));
        when(usuarioService.findUsuarioById(transacaoDTO.remetenteId())).thenReturn(remetente);
        when(usuarioService.findUsuarioById(transacaoDTO.destinatarioId())).thenReturn(destinatario);

        assertThatThrownBy(() -> transacaoService.validarTransacao(transacaoDTO, remetente))
            .isInstanceOf(SaldoInsuficienteException.class)
            .hasMessage("Saldo insuficiente.");
    }

    @Test
    public void testTransacaoUsuario_Lojista() {
        remetente.setTipoDeUsuario(TipoDeUsuario.LOJISTA);
        when(usuarioService.findUsuarioById(transacaoDTO.remetenteId())).thenReturn(remetente);
        when(usuarioService.findUsuarioById(transacaoDTO.destinatarioId())).thenReturn(destinatario);

        assertThatThrownBy(() -> transacaoService.validarTransacao(transacaoDTO, remetente))
            .isInstanceOf(UsuarioNaoAutorizadoException.class)
            .hasMessage("Usuário do tipo \"LOJISTA\" não pode realizar esse tipo de transação.");
    }

    @Test
    public void testTransacaoEntreMesmosUsuariosException() {
        transacaoDTO = new DadosCadastroTransacaoDTO(1L, 1L, BigDecimal.TEN);
        when(usuarioService.findUsuarioById(transacaoDTO.remetenteId())).thenReturn(remetente);

        assertThatThrownBy(() -> transacaoService.validarTransacao(transacaoDTO, remetente))
            .isInstanceOf(TransacaoEntreMesmosUsuariosException.class)
            .hasMessage("O remetente deve ser diferente do destinatário para realizar a transação.");
    }

    @Test
    public void testValidarTransacao() {
        when(usuarioService.findUsuarioById(transacaoDTO.remetenteId())).thenReturn(remetente);
        when(usuarioService.findUsuarioById(transacaoDTO.destinatarioId())).thenReturn(destinatario);

        transacaoService.validarTransacao(transacaoDTO, remetente);

        // Se não houver exceções, o teste passará
        assertThat(true).isTrue();
    }

    @Test
    public void testFindTransacaoById() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);

        when(usuarioService.findUsuarioById(1L)).thenReturn(usuario1);
        when(usuarioService.findUsuarioById(2L)).thenReturn(usuario2);

        transacao.setRemetenteId(usuario1);
        transacao.setDestinatarioId(usuario2);

        when(transacaoRepository.findById(1L)).thenReturn(java.util.Optional.of(transacao));

        Usuario encontrarUsuarios = usuarioService.findUsuarioById(1L);
        assertThat(encontrarUsuarios).isNotNull();
        assertThat(encontrarUsuarios.getId()).isEqualTo(transacao.getRemetenteId().getId());
        verify(usuarioService, times(1)).findUsuarioById(usuario1.getId());

        encontrarUsuarios = usuarioService.findUsuarioById(2L);
        assertThat(encontrarUsuarios).isNotNull();
        assertThat(encontrarUsuarios.getId()).isEqualTo(transacao.getDestinatarioId().getId());
        verify(usuarioService, times(1)).findUsuarioById(usuario2.getId());

        Transacao encontrarTransacao = transacaoService.findTransacaoById(1L);
        assertThat(encontrarTransacao).isNotNull();
        assertThat(encontrarTransacao.getId()).isEqualTo(transacao.getId());
        verify(transacaoRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAllTransacoes() {
        when(transacaoRepository.findAll()).thenReturn(List.of(transacao));
        List<Transacao> transacoes = transacaoService.findAllTransacoes();
        assertThat(transacoes).isNotEmpty();
        assertThat(transacoes.get(0).getId()).isEqualTo(1L);
        verify(transacaoRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllByAtivoTrue() {
        when(transacaoRepository.findAllByAtivoTrue()).thenReturn(List.of(transacao));
        List<Transacao> transacaos = transacaoService.findAllByAtivoTrue();
        assertThat(transacaos).isNotEmpty();
        assertThat(transacaos.get(0).getId()).isEqualTo(1L);
        verify(transacaoRepository, times(1)).findAllByAtivoTrue();
    }

    @Test
    public void testDeletarTransacaoById() {
        when(transacaoRepository.findById(1L)).thenReturn(Optional.of(transacao));
        this.transacaoRepository.deleteById(transacao.getId());
        verify(transacaoRepository, times(1)).deleteById(1L);
    }
}
