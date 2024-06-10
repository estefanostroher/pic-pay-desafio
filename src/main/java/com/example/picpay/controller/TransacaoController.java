package com.example.picpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.picpay.dto.DadosCadastroTransacaoDTO;
import com.example.picpay.dto.DadosListagemTransacaoDTO;
import com.example.picpay.service.TransacaoService;
import com.example.picpay.transacao.Transacao;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    /**
     * @author Estefano Ströher
     * @param transacao Dados para criar uma nova transação.
     * @return 201 CREATED + Dados da transação criada
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes
     * </p>
     * 
     * <p>
     * Exemplo de uso:
     * <pre>
     * {@code
     * {
     *     "remetenteId": 1,
     *     "destinatarioId": 2,
     *     "valor": 100.00,
     * }
     * }
     * </pre>
     *
     */
    @PostMapping()
    @Transactional
    public ResponseEntity<DadosListagemTransacaoDTO> criarTransacao(@RequestBody DadosCadastroTransacaoDTO transacao) {
        Transacao novaTransacao = this.transacaoService.criarTransacao(transacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DadosListagemTransacaoDTO(novaTransacao));
    }

    /**
     * @author Estefano Ströher
     * @param id Id da transação que deseja buscar.
     * @return 200 OK + Dados da transação buscados pelo id
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/5000
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 5000,
     *     "remetenteId": 1,
     *     "destinatarioId": 2,
     *     "valor": 100.00,
     *     "data": "2022-01-01T00:00:00",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemTransacaoDTO> listarTransacaoPorId(@PathVariable Long id) {
        var transacao = this.transacaoService.findTransacaoById(id);
        return ResponseEntity.ok(new DadosListagemTransacaoDTO(transacao));
    }

    /**
     * @author Estefano Ströher
     * @return 200 OK + Dados de todas as transações
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 1,
     *     "remetenteId": 1,
     *     "destinatarioId": 2,
     *     "valor": 100.00,
     *     "data": "2022-01-01T00:00:00",
     *     "ativo": true | false
     * }
     * }
     * </pre>
     *
     */
    @GetMapping
    public ResponseEntity<List<DadosListagemTransacaoDTO>> listarTodasTransacoes() {
        var transacoes = this.transacaoService.findAllTransacoes();
        return ResponseEntity.ok(transacoes.stream().map(DadosListagemTransacaoDTO::new).toList());
    }

    /**
     * @author Estefano Ströher
     * @return 200 OK + Dados de todas as transações ativas
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/ativas
     * </p>
     * 
     * <p>
     * Exemplo de retorno:
     * <pre>
     * {@code
     * {
     *     "id": 10,
     *     "remetenteId": 12,
     *     "destinatarioId": 23,
     *     "valor": 50.00,
     *     "data": "2022-01-01T00:00:00",
     *     "ativo": true
     * }
     * }
     * </pre>
     *
     */
    @GetMapping("ativas")
    public ResponseEntity<List<DadosListagemTransacaoDTO>> listarTodasTransacoesAtivas() {
        var lista = this.transacaoService.findAllByAtivoTrue();
        return ResponseEntity.ok(lista.stream().map(DadosListagemTransacaoDTO::new).toList());
    }

    /**
     * @author Estefano Ströher
     * @param id Id da transação que deseja deletar.
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/1
     * </p>
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarTransacaoPorId(@PathVariable Long id) {
        this.transacaoService.deletarTransacaoById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @author Estefano Ströher
     * @param id Id da transação que deseja inativar.
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/inativar-transacao/1
     * </p>
     */
    @DeleteMapping("inativar-transacao/{id}")
    @Transactional
    public ResponseEntity<Void> inativarTransacao(@PathVariable Long id) {
        var transacao = this.transacaoService.findTransacaoById(id);
        transacao.inativarTransacao();
        return ResponseEntity.noContent().build();
    }

    /**
     * @author Estefano Ströher
     * @param id Id da transação que deseja ativar.
     * @return 404 NO CONTENT
     * 
     * <p>
     * Exemplo de uso: http://localhost:8080/transacoes/ativar-transacao/1
     * </p>
     */
    @PutMapping("ativar-transacao/{id}")
    @Transactional
    public ResponseEntity<Void> ativarTransacao(@PathVariable Long id) {
        var transacao = this.transacaoService.findTransacaoById(id);
        transacao.ativarTransacao();
        return ResponseEntity.noContent().build();
    }
}
