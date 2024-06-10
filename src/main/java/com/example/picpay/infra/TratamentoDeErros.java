package com.example.picpay.infra;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratamentoDeErros {

    /**
     * Tratamento utilizado quando o preenchimento dos campos for inválido.
     * @param exception
     * @return 400 Bad Resquest
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratamento400BadRequest(MethodArgumentNotValidException exception) {
        var erro = exception.getFieldErrors();
        return ResponseEntity.badRequest().body(erro.stream().map(MensagemDosErros::new).toList());
    }

    /**
     * Tratamento utilizado quando o cliente não está autorizado a acessar o recurso.
     * @param exception
     * @return 401 Unauthorized
     */
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> tratamento401Unauthorized(HttpClientErrorException.Unauthorized exception) {
        return ResponseEntity.status(401).body(Map.of("mensagem", "Não autorizado", "detalhes", exception.getMessage()));
    }

    /**
     * Tratamento utilizado quando o cliente não está cadastrado no sistema.
     * Também utilizado quando o login no sistema for inválido, etc.
     * @param exception
     * @return 403 Forbidden
     */

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<?> tratamento403Forbidden(HttpClientErrorException.Forbidden exception) {
        return ResponseEntity.status(403).body(Map.of("mensagem", "Acesso proibido", "detalhes", exception.getMessage()));
    }

    /**
     * Tratamento utilizado quando o dado buscado não for encontrado.
     * @param exception
     * @return 404 Not Found
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> tratamento404NotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(404).body(Map.of("mensagem", "Recurso não encontrado", "detalhes", exception.getMessage()));
    }

    /**
     * Tratamento utilizado quando o método HTTP não é suportado.
     * @param exception
     * @return 405 Method Not Allowed
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> tratamento405MethodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(405).body(Map.of("mensagem", "Método não permitido", "detalhes", exception.getMessage()));
    }

    /**
     * Tratamento utilizado quando ocorre um erro interno no servidor.
     * @param exception
     * @return 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> tratamento500InternalServerError(Exception exception) {
        return ResponseEntity.status(500).body(Map.of("mensagem", "Erro interno do servidor", "detalhes", exception.getMessage()));
    }
}
