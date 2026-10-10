package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamentoErrosApi {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroApiDTO> tratarDadosInvalidos(
            IllegalArgumentException erro) {

        ErroApiDTO resposta = new ErroApiDTO(
                400,
                "Bad Request",
                erro.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroApiDTO> tratarNaoEncontrado(
            RecursoNaoEncontradoException erro) {

        ErroApiDTO resposta = new ErroApiDTO(
                404,
                "Not Found",
                erro.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroApiDTO> tratarErroInterno(Exception erro) {

        ErroApiDTO resposta = new ErroApiDTO(
                500,
                "Internal Server Error",
                "Ocorreu um erro interno ao processar a requisição."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resposta);
    }
}