package com.example.picpay.resources;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.picpay.Application;
import com.example.picpay.dto.DadosListagemNotificacaoDTO;
import com.example.picpay.notification.NotificacaoProducer;
import com.example.picpay.service.NotificacaoService;
import com.example.picpay.transacao.Transacao;
import com.example.picpay.usuario.Usuario;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class NotificacaoServiceTest {

    @Mock
    private NotificacaoProducer notificacaoProducer;

    @InjectMocks
    private NotificacaoService notificacaoService;

    @Mock
    private Transacao transacao;

    @Mock
    private Usuario remetente, destinatario;

    @BeforeEach
    void setUp() {
        remetente = new Usuario();
        remetente.setId(1L);
        remetente.setNomeCompleto("Teste 1");

        destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNomeCompleto("Teste 2");
        
        transacao = new Transacao();
        transacao.setId(1L);
        transacao.setRemetenteId(remetente);
        transacao.setDestinatarioId(destinatario);
        transacao.setAtivo(true);
        transacao.setValor(BigDecimal.valueOf(100));
        transacao.setData(LocalDateTime.now());
    }

    @Test
    void testNotificarTransacao() {
        DadosListagemNotificacaoDTO notificacaoDTO = new DadosListagemNotificacaoDTO(transacao, remetente);
        notificacaoService.notificarTransacao(notificacaoDTO);
        verify(notificacaoProducer, times(1)).enviarNotificacao(notificacaoDTO);
    }
}
