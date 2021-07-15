package br.com.bootcamp.desafio_quality.exception.handler;

import br.com.bootcamp.desafio_quality.dto.MensagemDeErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> defaultHandler(){
        return ResponseEntity.internalServerError().body(new MensagemDeErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error"));
    }

}
