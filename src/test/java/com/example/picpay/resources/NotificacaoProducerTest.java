package com.example.picpay.resources;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.picpay.Application;
import com.example.picpay.dto.DadosListagemNotificacaoDTO;
import com.example.picpay.notification.NotificacaoProducer;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class NotificacaoProducerTest {

    @Mock
    private KafkaTemplate<String, DadosListagemNotificacaoDTO> kafkaTemplate;

    @InjectMocks
    private NotificacaoProducer notificacaoProducer;

    @Mock
    private DadosListagemNotificacaoDTO dadosListagemNotificacaoDTO;

    @BeforeEach
    void setUp() {
        dadosListagemNotificacaoDTO = new DadosListagemNotificacaoDTO("Teste 1", BigDecimal.valueOf(100), LocalDateTime.now());
    }

    @Test
    void testEnviarNotificacao() {
        notificacaoProducer.enviarNotificacao(dadosListagemNotificacaoDTO);
        verify(kafkaTemplate, times(1)).send("transacao-notificacao", dadosListagemNotificacaoDTO);
    }
}


