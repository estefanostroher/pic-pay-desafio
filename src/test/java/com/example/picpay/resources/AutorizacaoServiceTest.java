package com.example.picpay.resources;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.example.picpay.Application;
import com.example.picpay.dto.AutorizacaoDTO;
import com.example.picpay.dto.DadosCadastroTransacaoDTO;
import com.example.picpay.infra.TransacaoNaoAutorizadaException;
import com.example.picpay.service.AutorizacaoService;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class})
public class AutorizacaoServiceTest {

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @Mock
    private AutorizacaoService autorizacaoService;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        autorizacaoService = new AutorizacaoService(restClientBuilder);
    }

    @Test
    void testAutorizarTransacao_Autorizado() {
        DadosCadastroTransacaoDTO transacao = new DadosCadastroTransacaoDTO(1L, 2L, BigDecimal.TEN);
        AutorizacaoDTO autorizacaoDTO = new AutorizacaoDTO("Autorizado");
        ResponseEntity<AutorizacaoDTO> responseEntity = ResponseEntity.ok(autorizacaoDTO);

        when(restClient.get()).thenReturn((RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(AutorizacaoDTO.class)).thenReturn(responseEntity);

        assertDoesNotThrow(() -> autorizacaoService.autorizarTransacao(transacao));
    }

    @Test
    void testAutorizarTransacao_NaoAutorizado() {
        DadosCadastroTransacaoDTO transacao = new DadosCadastroTransacaoDTO(1L, 2L, BigDecimal.TEN);
        AutorizacaoDTO autorizacaoDTO = new AutorizacaoDTO("Não Autorizado");
        ResponseEntity<AutorizacaoDTO> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(autorizacaoDTO);

        when(restClient.get()).thenReturn((RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(AutorizacaoDTO.class)).thenReturn(responseEntity);

        assertThrows(TransacaoNaoAutorizadaException.class, () -> autorizacaoService.autorizarTransacao(transacao));
    }

    @Test
    void testAutorizarTransacao_Erro_LancarException() {
        DadosCadastroTransacaoDTO transacao = new DadosCadastroTransacaoDTO(1L, 2L, BigDecimal.TEN);

        when(restClient.get()).thenReturn((RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(AutorizacaoDTO.class)).thenThrow(new TransacaoNaoAutorizadaException("Erro no Serviço"));

        assertThrows(TransacaoNaoAutorizadaException.class, () -> autorizacaoService.autorizarTransacao(transacao));
    }
}

