package com.example.picpay.resources;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClient.Builder;

import com.example.picpay.Application;
import com.example.picpay.dto.NotificacaoDTO;
import com.example.picpay.infra.NotificacaoException;
import com.example.picpay.notification.NotificacaoConsumer;
import com.example.picpay.transacao.Transacao;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class NotificacaoConsumerTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RequestBodySpec requestBodySpec;

    @Mock
    private ResponseSpec responseSpec;

    @Mock
    private Builder restClientBuilder;

    @Mock
    private NotificacaoConsumer notificacaoConsumer;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);

        notificacaoConsumer = new NotificacaoConsumer(restClientBuilder);
    }

    @Test
    void testReceberNotificacao_Successo() {
        Transacao transacao = new Transacao();
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO(true);
        ResponseEntity<NotificacaoDTO> responseEntity = new ResponseEntity<>(notificacaoDTO, HttpStatus.OK);

        when(responseSpec.toEntity(NotificacaoDTO.class)).thenReturn(responseEntity);

        notificacaoConsumer.receberNotificacao(transacao);

        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).toEntity(NotificacaoDTO.class);
    }

    @Test
    void testReceberNotificacao_Falha() {
        Transacao transacao = new Transacao();
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO(false);
        ResponseEntity<NotificacaoDTO> responseEntity = new ResponseEntity<>(notificacaoDTO, HttpStatus.OK);

        when(responseSpec.toEntity(NotificacaoDTO.class)).thenReturn(responseEntity);

        assertThrows(NotificacaoException.class, () -> notificacaoConsumer.receberNotificacao(transacao));

        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).toEntity(NotificacaoDTO.class);
    }

    @Test
    void testReceberNotificacao_Error() {
        Transacao transacao = new Transacao();
        ResponseEntity<NotificacaoDTO> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(responseSpec.toEntity(NotificacaoDTO.class)).thenReturn(responseEntity);

        assertThrows(NotificacaoException.class, () -> notificacaoConsumer.receberNotificacao(transacao));

        verify(restClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).toEntity(NotificacaoDTO.class);
    }
}
